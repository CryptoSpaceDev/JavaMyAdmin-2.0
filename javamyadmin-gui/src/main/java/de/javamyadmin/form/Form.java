package de.javamyadmin.form;

import de.javamyadmin.FontAwesome;
import de.javamyadmin.config.FormVerifier;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Form extends BorderPane {

    private static final Logger log = LoggerFactory.getLogger(Form.class);

    private final ObservableList<FormNode> nodes = FXCollections.observableArrayList();
    private final GridPane gridPane = new GridPane();
    private final Label errorLabel = new Label();
    private final Button rollbackButton = new Button("Rollback", FontAwesome.FA_UNDO.build());
    private final Button submitButton = new Button("Submit", FontAwesome.FA_CHECK.build());
    private final List<FormVerifier> verifiers = new ArrayList<>();
    private final List<Runnable> onSubmit = new ArrayList<>();

    public Form(FormNode... nodes) {
        this.nodes.addAll(nodes);
        this.nodes.addListener((ListChangeListener<? super FormNode>) change -> refreshLayout());

        initView();
    }

    private void initView() {
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setMinWidth(100.0);
        gridPane.getColumnConstraints().addAll(column1);

        gridPane.setPadding(new Insets(10.0));
        gridPane.setHgap(10.0);
        gridPane.setVgap(5.0);

        TilePane buttonLayout = new TilePane(rollbackButton, submitButton);
        buttonLayout.setPadding(new Insets(10.0));
        buttonLayout.setHgap(10.0);
        buttonLayout.setAlignment(Pos.BASELINE_RIGHT);

        VBox mainFormLayout = new VBox(gridPane, errorLabel);
        setCenter(mainFormLayout);
        setBottom(buttonLayout);

        errorLabel.setGraphic(FontAwesome.FA_EXCLAMATION_TRIANGLE.build(Color.RED));
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
        errorLabel.setPadding(new Insets(10.0));
        errorLabel.setWrapText(true);

        rollbackButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        rollbackButton.setOnAction(event -> rollback());

        submitButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        submitButton.setOnAction(event -> {
            errorLabel.setVisible(false);

            try {
                commit();

                for (Runnable runnable : onSubmit) {
                    runnable.run();
                }
            } catch (InvalidValueException e) {
                log.error(e.getMessage(), e);
                errorLabel.setVisible(true);
                errorLabel.setText(e.getMessage());
            }
        });

        minWidthProperty().bind(mainFormLayout.minWidthProperty());
        minHeightProperty().bind(mainFormLayout.minHeightProperty().add(buttonLayout.minHeightProperty()));
    }

    private void refreshLayout() {
        int rowIndex = 0;

        gridPane.getChildren().clear();

        for (FormNode node : nodes) {
            if (node.getLabel() instanceof Separator separator && separator.getOrientation() == Orientation.HORIZONTAL) {
                gridPane.add(node.getLabel(), 0, rowIndex, GridPane.REMAINING, 1);
            } else {
                gridPane.addRow(rowIndex, node.getLabel(), node.getNode());
            }

            rowIndex++;
        }
    }

    public ObservableList<FormNode> getNodes() {
        return nodes;
    }

    public void addVerifier(FormVerifier verifier) {
        verifiers.add(verifier);
    }

    public void addSubmitListener(Runnable runnable) {
        onSubmit.add(runnable);
    }

    public Label getErrorLabel() {
        return errorLabel;
    }

    public Button getRollbackButton() {
        return rollbackButton;
    }

    public Button getSubmitButton() {
        return submitButton;
    }

    public void add(FormNode... node) {
        this.nodes.addAll(node);
    }

    public void addSeparator() {
        this.nodes.add(new FormSeparator());
    }

    public void verify() throws InvalidValueException {
        for (FormNode node : nodes) {
            node.verify();
        }

        for (FormVerifier verifier : verifiers) {
            verifier.verify();
        }
    }

    public void commit() throws InvalidValueException {
        verify();

        for (FormNode node : nodes) {
            node.commit();
        }
    }

    public void rollback() {
        for (FormNode node : nodes) {
            node.rollback();
        }
    }

}
