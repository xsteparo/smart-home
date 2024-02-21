package cz.cvut.fel.omo.house.room;

import cz.cvut.fel.omo.entity.device.Curtain;

/**
 * The type Window.
 */
public class Window {

    private Curtain curtain;

    /**
     * Gets curtain.
     *
     * @return the curtain
     */
    public Curtain getCurtain() {
        return curtain;
    }

    /**
     * Set curtain window.
     *
     * @param curtain the curtain
     * @return the window
     */
    public Window setCurtain(Curtain curtain){
        this.curtain = curtain;
        return this;
    }
}
