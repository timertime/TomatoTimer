package view;

import eventmanagement.Event;
import eventmanagement.Observer;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import model.TimerModel;
import model.events.DurationRemainingUpdateEvent;
import model.events.TimerResetEvent;
import utility.TimeFormatter;

import java.time.Duration;

public class TimerView implements Observer<DurationRemainingUpdateEvent>{

    private static final String START_TIME = "25:00";
    private final TimerModel timerModel;
    private HBox timerHolder;
    private Label timeElapsed;

    TimerView(TimerModel timerModel) {
        this(timerModel, new HBox(), new Label(START_TIME));
    }

    private TimerView(TimerModel timerModel, HBox timerHolder, Label timeElapsed) {
        this.timerHolder = timerHolder;
        this.timeElapsed = timeElapsed;
        this.timerModel = timerModel;
        timerModel.registerFor(DurationRemainingUpdateEvent.class, this::handleDurationRemainingUpdate);
        timerModel.registerFor(TimerResetEvent.class, this::handleReset);
        timerHolder.getChildren().add(timeElapsed);
        timerHolder.setAlignment(Pos.CENTER);
    }

    private  void handleReset(TimerResetEvent resetEvent) {
        Duration duration = timerModel.getTimerDuration();
        updateDisplayText(duration);
    }

    public HBox getNode() {
        return timerHolder;
    }

    @Override
    public void handleDurationRemainingUpdate(DurationRemainingUpdateEvent event) {
        updateDisplayText(event.getDuration());
    }

    private void updateDisplayText(Duration duration) {
        String durationText = new TimeFormatter().formatDuration(duration);
        Platform.runLater(() -> timeElapsed.setText(durationText));
    }
}