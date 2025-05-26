package me.itzisonn_.display.manager;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Display;

@Getter
@Setter
public class DisplayData<T extends Display> {
    private int id;
    private final T display;

    public DisplayData(int id, T display) {
        this.id = id;
        this.display = display;
    }
}
