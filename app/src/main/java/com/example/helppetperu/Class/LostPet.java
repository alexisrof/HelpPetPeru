package com.example.helppetperu.Class;

import java.util.ArrayList;

public class LostPet extends Pet {

    ArrayList<ReportLocation> ListReportLocation;

    public LostPet(){
    }

    public LostPet(String id, String nombre, String nacimiento, String detalle, String tipoMascota, String origenMascota, String estadoMascota, String imgUrl, String propietario, ArrayList<ReportLocation> listReportLocation) {
        super(id, nombre, nacimiento, detalle, tipoMascota, origenMascota, estadoMascota, imgUrl, propietario);
        this.ListReportLocation = listReportLocation;
    }

    public ArrayList<ReportLocation> getListReportLocation() {
        return ListReportLocation;
    }

    public void setListReportLocation(ArrayList<ReportLocation> listReportLocation) {
        ListReportLocation = listReportLocation;
    }

    public void addElement(ReportLocation replocation){
        ListReportLocation.add(replocation);
    }
}
