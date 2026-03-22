package cz.vse.semestralka_4it115.main;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.function.Function;

/**
 * JavaFX cell renderer for item rows with icon and weight.
 */
public class ItemListCell extends ListCell<ItemListEntry> {
    private final Function<String, Image> iconLoader;
    private final ImageView icon = new ImageView();
    private final Label nameLabel = new Label();
    private final Label weightLabel = new Label();
    private final HBox content = new HBox(8, icon, nameLabel, weightLabel);

    public ItemListCell(Function<String, Image> iconLoader) {
        this.iconLoader = iconLoader;
        icon.setFitWidth(24);
        icon.setFitHeight(24);
        icon.setPreserveRatio(true);
        weightLabel.setStyle("-fx-text-fill: #555;");
    }

    @Override
    protected void updateItem(ItemListEntry item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
            return;
        }

        nameLabel.setText(item.name());
        icon.setImage(iconLoader.apply(item.placeholder() ? null : item.name()));
        weightLabel.setText(item.placeholder() ? "" : String.format("(%.1f kg)", item.weight()));
        setText(null);
        setGraphic(content);
    }
}
