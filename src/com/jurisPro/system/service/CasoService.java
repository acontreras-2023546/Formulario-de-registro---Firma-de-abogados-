
package com.jurisPro.system.service;

import com.jurisPro.system.repository.CasoRepository;

public class CasoService {

    private final CasoRepository casoRepository;

    public CasoService() {
        casoRepository = new CasoRepository();
    }

    public String crearCaso(String descripcion,
                            String estado,
                            String informe,
                            String fecha,
                            String idAbogado) {

        return casoRepository.insertarCaso(
                descripcion,
                estado,
                informe,
                fecha,
                idAbogado
        );
    }

    public boolean eliminarCaso(String idCaso) {
        return casoRepository.eliminarCaso(idCaso);
    }
}
































//package com.jurisPro.system.service;
//
//import com.jurisPro.system.repository.CasoRepository;
//import java.util.List;
//
//public class CasoService {
//
//    private final CasoRepository casoRepository;
//
//    public CasoService() {
//        casoRepository = new CasoRepository();
//    }
//
//    public boolean crearCaso(String description, String status,
//                             String informe, String date,
//                             String idAbogado) {
//
//        return casoRepository.insertar(
//                description,
//                status,
//                informe,
//                date,
//                idAbogado
//        );
//    }
//
//    public List<String[]> obtenerCasos() {
//
//        return casoRepository.listar();
//    }
//
//    public boolean actualizarCaso(String idCaso, String description,
//                                  String status, String informe,
//                                  String date, String idAbogado) {
//
//        return casoRepository.actualizar(
//                idCaso,
//                description,
//                status,
//                informe,
//                date,
//                idAbogado
//        );
//    }
//
//    public boolean eliminarCaso(String idCaso) {
//
//        return casoRepository.eliminar(idCaso);
//    }
//}