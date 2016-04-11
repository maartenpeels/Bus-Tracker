/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.busenzo.gui.markers;

import com.busenzo.domein.Bus;
import com.busenzo.domein.Lijn;
import com.busenzo.domein.Rit;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

public class BusMarker extends Marker {

    private final String iconPad = "http://37.97.149.53/busenzo/external/resources/bus.png";
    private String lijnID;

    public BusMarker(MarkerOptions markerOptions, Lijn l) {
        super(markerOptions);
        lijnID = l.getId();
        MarkerOptions markerOptions2 = new MarkerOptions();
        for(Rit r : l.getRitten())
        {
            Bus b = r.getBus();
            if(b != null)
            {
                double cordsX = b.getCoordinaten()[0];
                double cordsY = b.getCoordinaten()[1];
                LatLong mappos = new LatLong(cordsX, cordsY);
                markerOptions2.icon(iconPad);
                markerOptions2.position(mappos);
                markerOptions2.title(l.getBeschrijving());
                super.setOptions(markerOptions2);
            }
        }
        this.setVisible(false);
    }
    
    public String getLijnID(){
        return lijnID;
    }
}
