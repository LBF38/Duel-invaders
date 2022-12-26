package org.enstabretagne.Component;

import org.enstabretagne.Core.Alien;

import com.almasb.fxgl.entity.component.Component;

public class AlienComponent extends Component {
    private Alien alien;

    public AlienComponent(Alien alien) {
        this.alien = alien;
    }

    @Override
    public void onUpdate(double tpf) {
        // FIXME: the alien is moving out of bounds.
        // This is because we update `translateX` and `translateY` after the alien has moved.
        // We should update the position of the component with getX and getY.
        alien.move();
        this.entity.translateX(alien.getX());
        this.entity.translateY(alien.getY());
    }
}
