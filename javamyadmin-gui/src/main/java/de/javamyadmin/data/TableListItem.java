package de.javamyadmin.data;

import de.javamyadmin.FontAwesome;

public record TableListItem(FontAwesome icon, String name) {

    @Override
    public String toString() {
        return name;
    }

}
