package org.enstabretagne.Component;

import com.almasb.fxgl.entity.component.Component;

public class PlayerComponent extends Component {
    private Double x, y;
    
    public void shoot() {
        System.out.println("Player Shoot");
    }
}
