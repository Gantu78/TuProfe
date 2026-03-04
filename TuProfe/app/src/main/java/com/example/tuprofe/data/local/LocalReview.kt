package com.example.tuprofe.data.local


import com.example.tuprofe.R
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.local.LocalProfesor.profesores


object LocalReview {

    val Reviews = listOf(

        ReviewInfo(
            reviewId = 1,
            profesor = profesores[1],
            username = "Golosina33",
            likes = 20,
            content = "Gran profe me parece un muy bueno.",
            time = "8:26 PM",
            rating = 4,
            comments = 0,
        ),

        ReviewInfo(
            reviewId = 2,
            profesor = profesores[0],
            username = "Pablosexto",
            likes = 100,
            content = "El profesor es un excelente maestro, me parece un muy buen maestro, se toma su tiempo para explicar hasta que todos entiendan el tema, no califica muy duro.",
            time = "8:26 PM",
            rating = 3,
            comments = 3,
        ),

        ReviewInfo(
            reviewId = 3,
            profesor = profesores[6],
            username = "JuanGuti",
            likes = 1,
            content = "El profe me parece un muy buen maestro, se toma su tiempo para explicar hasta que todos entiendan el tema.",
            time = "8:26 PM",
            rating = 2,
            comments = 10,
        ),

        ReviewInfo(
            reviewId = 4,
            profesor = profesores[0],
            username = "HQL",
            likes = 1000,
            content = "Explica bien pero las evaluaciones son difíciles.",
            time = "8:26 PM",
            rating = 1,
            comments = 100,
        ),

        ReviewInfo(
            reviewId = 5,
            profesor = profesores[2],
            username = "Jp23",
            likes = 10,
            content = "Buen profesor para empezar a programar.",
            time = "8:26 PM",
            rating = 5,
            comments = 1,
        ),

        ReviewInfo(
            reviewId = 6,
            profesor = profesores[3],
            username = "MariaGarcia",
            likes = 45,
            content = "Excelente explicación de las derivadas, muy paciente.",
            time = "10:15 AM",
            rating = 5,
            comments = 2,
        ),

        ReviewInfo(
            reviewId = 7,
            profesor = profesores[4],
            username = "PedroPerez",
            likes = 12,
            content = "Las tareas son largas pero se aprende mucho.",
            time = "2:30 PM",
            rating = 4,
            comments = 5,
        ),

        ReviewInfo(
            reviewId = 8,
            profesor = profesores[5],
            username = "LuciaMendez",
            likes = 8,
            content = "Muy aburrida la clase, solo lee las diapositivas.",
            time = "11:00 AM",
            rating = 2,
            comments = 1,
        ),

        ReviewInfo(
            reviewId = 9,
            profesor = profesores[1],
            username = "RobertoGomez",
            likes = 67,
            content = "La mejor profesora que he tenido.",
            time = "4:45 PM",
            rating = 5,
            comments = 12,
        ),

        ReviewInfo(
            reviewId = 10,
            profesor = profesores[7],
            username = "ElenaRivas",
            likes = 3,
            content = "Explica muy rápido y es difícil seguirle el ritmo.",
            time = "9:00 AM",
            rating = 3,
            comments = 0,
        )
    )
}
