package com.example.touchscreen;

import java.util.ArrayList;
import java.util.List;

public class DebugList {
    public List<DebugLine> debugLines;
    public DebugList()
    {
        debugLines=new ArrayList<DebugLine>();
    }
    //Actualiza el valor del valor asociado con NAME.. si no existe, NO crea uno nuevo
    public void u(String name,int Value){
        update(name,Value, false);
    }
    //Crea un valor asociado con NAME... si ya existe, actualiza el existente
    public void a(String name,int Value){
        update(name,Value, true);
    }
    //Crea un valor asociado con NAME... si ya existe, actualiza el existente
    public void add(String name,int Value){
        update(name,Value, true);
    }
    public void u(String name,int Value,boolean createNew){
        update(name, Value, createNew);
    }
    public void update(String name, int Value, boolean createNew){
        int index=search(name);
        if(index<0){
            if(createNew)
            {
                debugLines.add(new DebugLine(name,Value));
            }
            return;
        }
        debugLines.get(index).Value=Value;
    }
    //Devuelve el indice de la variable de nombre NAME
    public int search(String name){
        for (int i = 0; i < debugLines.size(); i++) {
            if (debugLines.get(i).Name.equals(name))
                return i;
        }
        return -1;
    }
}
