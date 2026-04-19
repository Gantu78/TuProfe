package com.example.tuprofe.data.local


import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.Usuario
import com.example.tuprofe.data.local.LocalProfesor.profesores
import com.example.tuprofe.data.local.LocalMateria.materias


object LocalReview {

    private val userMock = Usuario(
        usuarioId = "1",
        nombreUsu = "Usuario Mock",
        email = "mock@example.com",
        carrera = "Ingeniería",
        imageprofeUrl = null
    )

    val Reviews = listOf(

        ReviewInfo(
            reviewId = "1",
            profesor = profesores[1],
            materia = materias[1],
            likes = 20,
            content = "Gran profe me parece un muy bueno.",
            time = "8:26 PM",
            rating = 4,
            commentsCount = 3,
            usuario = userMock,
        ),

        ReviewInfo(
            reviewId = "2",
            profesor = profesores[0],
            usuario = userMock,
            materia = materias[0],
            likes = 100,
            content = "El profesor es un excelente maestro, me parece un muy buen maestro, se toma su tiempo para explicar hasta que todos entiendan el tema, no califica muy duro.",
            time = "8:26 PM",
            rating = 3,
            commentsCount = 10,
        ),

        ReviewInfo(
            reviewId = "3",
            profesor = profesores[6],
            usuario = userMock,
            materia = materias[3],
            likes = 1,
            content = "El profe me parece un muy buen maestro, se toma su tiempo para explicar hasta que todos entiendan el tema.",
            time = "8:26 PM",
            rating = 2,
            commentsCount = 2,
        ),

        ReviewInfo(
            reviewId = "4",
            profesor = profesores[0],
            usuario = userMock,
            materia = materias[0],
            likes = 1000,
            content = "Explica bien pero las evaluaciones son difíciles.",
            time = "8:26 PM",
            rating = 1,
            commentsCount = 7,
        ),

        ReviewInfo(
            reviewId = "5",
            profesor = profesores[2],
            usuario = userMock,
            materia = materias[2],
            likes = 10,
            content = "Buen profesor para empezar a programar.",
            time = "8:26 PM",
            rating = 5,
            commentsCount = 4,
        ),

        ReviewInfo(
            reviewId = "6",
            profesor = profesores[3],
            usuario = userMock,
            materia = materias[7],
            likes = 45,
            content = "Excelente explicación de las derivadas, muy paciente.",
            time = "10:15 AM",
            rating = 5,
            commentsCount = 12,
        ),

        ReviewInfo(
            reviewId = "7",
            profesor = profesores[4],
            usuario = userMock,
            materia = materias[2],
            likes = 12,
            content = "Las tareas son largas pero se aprende mucho.",
            time = "2:30 PM",
            rating = 4,
            commentsCount = 5,
        ),

        ReviewInfo(
            reviewId = "8",
            profesor = profesores[5],
            usuario = userMock,
            materia = materias[4],
            likes = 8,
            content = "Muy aburrida la clase, solo lee las diapositivas.",
            time = "11:00 AM",
            rating = 2,
            commentsCount = 1,
        ),

        ReviewInfo(
            reviewId = "9",
            profesor = profesores[1],
            usuario = userMock,
            materia = materias[1],
            likes = 67,
            content = "El mejor profesor que he tenido.",
            time = "4:45 PM",
            rating = 5,
            commentsCount = 9,
        ),

        ReviewInfo(
            reviewId = "10",
            profesor = profesores[7],
            usuario = userMock,
            materia = materias[5],
            likes = 3,
            content = "Explica muy rápido y es difícil seguirle el ritmo.",
            time = "9:00 AM",
            rating = 3,
            commentsCount = 16,
        )
    )
}
