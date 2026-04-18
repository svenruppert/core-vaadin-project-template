package com.svenruppert.flow.views.main;

import com.svenruppert.flow.MainLayout;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Route(value = PushDemoView.PATH, layout = MainLayout.class)
public class PushDemoView extends VerticalLayout {

  public static final String PATH = "pushDemo";

  private final Paragraph status = new Paragraph("Noch kein Push erfolgt");
  private final Button startButton = new Button("Push starten");

  private ScheduledExecutorService executor;

  public PushDemoView() {
    add(status, startButton);

    startButton.addClickListener(event -> startPushDemo());
  }

  private void startPushDemo() {
    if (executor != null && !executor.isShutdown()) {
      return;
    }

    UI ui = UI.getCurrent();

    executor = Executors.newSingleThreadScheduledExecutor();
    executor.scheduleAtFixedRate(() -> {
      if (ui.isAttached()) {
        ui.access(() -> status.setText("Server Push: " + LocalTime.now()));
      }
    }, 0, 1, TimeUnit.SECONDS);
  }

  @Override
  protected void onDetach(DetachEvent detachEvent) {
    if (executor != null) {
      executor.shutdownNow();
    }
    super.onDetach(detachEvent);
  }
}