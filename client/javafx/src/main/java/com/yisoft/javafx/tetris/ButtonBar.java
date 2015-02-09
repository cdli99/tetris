package com.yisoft.javafx.tetris;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

class ButtonBar extends HBox {
    ButtonBar(double spacing, Button... buttons) {
      super(spacing);
      getChildren().addAll(buttons);
      for (Button b: buttons) {
        HBox.setHgrow(b, Priority.ALWAYS);
        b.setMaxWidth(Double.MAX_VALUE);
      }
    }

    public void addButton(Button button) {
      HBox.setHgrow(button, Priority.ALWAYS);
      button.setMaxWidth(Double.MAX_VALUE);
      ObservableList<Node> buttons = getChildren();
      if (!buttons.contains(button)) {
        buttons.add(button);
      }
    }

    public void removeButton(Button button) {
      getChildren().remove(button);
    }

    @Override protected void layoutChildren() {
      double minPrefWidth = calculatePrefChildWidth();
      for (Node n: getChildren()) {
        if (n instanceof Button) {
          ((Button) n).setMinWidth(minPrefWidth);
        }
      }
      super.layoutChildren();
    }

    private double calculatePrefChildWidth() {
      double minPrefWidth = 0;
      for (Node n: getChildren()) {
        minPrefWidth = Math.max(minPrefWidth, n.prefWidth(-1));
      }
      return minPrefWidth;
    }
  }