package de.joblift.service.gitlabpanorama.resources.adapter.prometheus;

import de.galan.commons.logging.Say;
import de.joblift.service.gitlabpanorama.core.aggregator.Aggregator;
import de.joblift.service.gitlabpanorama.core.aggregator.PipelinePair;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Metrics adapter for a prometheus scraper
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/adapter/prometheus")
public class PrometheusExporter {

	private Aggregator aggregator;


	@RequestMapping(produces = {"text/plain"})
	public String bash() {
		Say.info("Requesting {format}", "prometheus");
		List<PipelinePair> pipelines = aggregator.getPipelines();
		return pipelines.stream()
			.filter(pair -> pair.getCurrent() != null)
			.map(pair -> "gitlab_pipeline_state{repository=\"" + pair.getName() + "\",ref=\"" + pair.getRef() + "\",state=\"" + pair.getCurrent()
				.getStatus() + "\"} " + stateToValue(pair))
			.collect(Collectors.joining("\n")) + "\n"; // requires line-break at end for newer prometheus versions
	}


	protected String stateToValue(PipelinePair pair) {
		return switch (pair.getCurrent().getStatus()) {
			case success, manual -> "0";
			case skipped -> "1";
			case canceled -> "2";
			case failed -> "10";
			default -> "-1";
		};
	}

}
