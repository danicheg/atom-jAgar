package zagar.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import zagar.Game;
import zagar.network.packets.PacketEjectMass;
import zagar.network.packets.PacketSplit;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class KeyboardListener implements KeyListener {

    private static final Logger LOG = LogManager.getLogger(KeyboardListener.class);

    @Override
    public void keyPressed(@NotNull KeyEvent event) {
        try {
            if (Game.socket.session.isOpen()) {
                if (event.getKeyCode() == KeyEvent.VK_SPACE) {
                    new PacketSplit(Game.login).write();
                }
                if (event.getKeyCode() == KeyEvent.VK_W) {
                    new PacketEjectMass(Game.followX, Game.followY, Game.login).write();
                }
            }
        } catch (IOException e) {
            LOG.error("Can't send packet because session is closed: " + e);
        }
    }

    @Override
    public void keyReleased(@NotNull KeyEvent e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException();
    }

}
