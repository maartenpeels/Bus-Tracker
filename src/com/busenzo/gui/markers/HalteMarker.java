/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.busenzo.gui.markers;

import com.busenzo.domein.Halte;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

public class HalteMarker extends Marker {

    private String iconPad = "http://37.97.149.53/busenzo/external/resources/busstop.png";
    private String halteID;
    public boolean active;

    public HalteMarker(MarkerOptions markerOptions, Halte h, boolean active) {
        super(markerOptions);
        this.halteID = h.getId();
        MarkerOptions markerOptions2 = new MarkerOptions();
        double cordsX = h.getCoordinaten()[0];
        double cordsY = h.getCoordinaten()[1];
        LatLong mappos = new LatLong(cordsX, cordsY);
        this.active = active;
        if(!active)
        iconPad = "http://i.imgur.com/8pC5LJs.png";
        markerOptions2.icon(iconPad);
        markerOptions2.position(mappos);
        markerOptions2.title(h.getNaam());
        super.setOptions(markerOptions2);
        this.setVisible(false);
    }
    
    public String getHalteID(){
        return this.halteID;
    }
}
