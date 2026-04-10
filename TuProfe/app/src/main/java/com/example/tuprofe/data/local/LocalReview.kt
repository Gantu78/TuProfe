package com.example.tuprofe.data.local


import com.example.tuprofe.R
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.local.LocalProfesor.profesores
import com.example.tuprofe.data.local.LocalMateria.materias


object LocalReview {

    val Reviews = listOf(

        ReviewInfo(
            reviewId = "1",
            profesor = profesores[1],
            username = "Golosina33",
            materia = materias[1],
            likes = 20,
            content = "Gran profe me parece un muy bueno.",
            time = "8:26 PM",
            rating = 4,
            comments = 0,
        ),

        ReviewInfo(
            reviewId = "2",
            profesor = profesores[0],
            username = "Pablosexto",
            materia = materias[0],
            likes = 100,
            content = "El profesor es un excelente maestro, me parece un muy buen maestro, se toma su tiempo para explicar hasta que todos entiendan el tema, no califica muy duro.",
            time = "8:26 PM",
            rating = 3,
            comments = 3,
        ),

        ReviewInfo(
            reviewId = "3",
            profesor = profesores[6],
            username = "JuanGuti",
            materia = materias[3],
            likes = 1,
            content = "El profe me parece un muy buen maestro, se toma su tiempo para explicar hasta que todos entiendan el tema.",
            time = "8:26 PM",
            rating = 2,
            comments = 10,
        ),

        ReviewInfo(
            reviewId = "4",
            profesor = profesores[0],
            username = "HQL",
            materia = materias[0],
            likes = 1000,
            content = "Explica bien pero las evaluaciones son difíciles.",
            time = "8:26 PM",
            rating = 1,
            comments = 100,
        ),

        ReviewInfo(
            reviewId = "5",
            profesor = profesores[2],
            username = "Jp23",
            materia = materias[2],
            likes = 10,
            content = "Buen profesor para empezar a programar.",
            time = "8:26 PM",
            rating = 5,
            comments = 1,
        ),

        ReviewInfo(
            reviewId = "6",
            profesor = profesores[3],
            username = "MariaGarcia",
            materia = materias[7],
            likes = 45,
            content = "Excelente explicación de las derivadas, muy paciente.",
            time = "10:15 AM",
            rating = 5,
            comments = 2,
        ),

        ReviewInfo(
            reviewId = "7",
            profesor = profesores[4],
            username = "PedroPerez",
            materia = materias[2],
            likes = 12,
            content = "Las tareas son largas pero se aprende mucho.",
            time = "2:30 PM",
            rating = 4,
            comments = 5,
        ),

        ReviewInfo(
            reviewId = "8",
            profesor = profesores[5],
            username = "LuciaMendez",
            materia = materias[4],
            likes = 8,
            content = "Muy aburrida la clase, solo lee las diapositivas.",
            time = "11:00 AM",
            rating = 2,
            comments = 1,
        ),

        ReviewInfo(
            reviewId = "9",
            profesor = profesores[1],
            username = "RobertoGomez",
            materia = materias[1],
            likes = 67,
            content = "El mejor profesor que he tenido.",
            time = "4:45 PM",
            rating = 5,
            comments = 12,
        ),

        ReviewInfo(
            reviewId = "10",
            profesor = profesores[7],
            username = "ElenaRivas",
            materia = materias[5],
            likes = 3,
            content = "Explica muy rápido y es difícil seguirle el ritmo.",
            time = "9:00 AM",
            rating = 3,
            comments = 0,
        )
    )
}
