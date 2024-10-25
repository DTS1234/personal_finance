package personal.finance.tracking.summary.application.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import personal.finance.common.events.EventListener;
import personal.finance.tracking.asset.domain.events.AssetCreated;
import personal.finance.tracking.summary.application.UpdateSummaryWithNewAssetUseCase;
import personal.finance.tracking.summary.domain.SummaryId;
import personal.finance.tracking.summary.domain.SummaryRepository;


