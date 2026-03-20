package com.example.tuprofe.data.local

import com.example.tuprofe.data.Profesor
import com.example.tuprofe.R

object LocalProfesor {

    val profesores = listOf(

        Profesor(
            profeId = 1,
            nombreProfe = "Carlos Parra",
            imageprofeUrl = "https://img.lalr.co/cms/2017/06/16184524/1280x1440_CARLOS-PARRA.jpg?r=6_5&ns=1&w=372&d=2.625",
        ),

        Profesor(
            profeId = 2,
            nombreProfe = "Juan Sebastian Angarita",
            imageprofeUrl = "https://ingenieria.javeriana.edu.co/documents/d/ingenieria/im-juan-angarita-2026-jpg-2?imagePreview=1"
        ),

        Profesor(
            profeId = 3,
            nombreProfe = "Andrés Martínez",
            imageprofeUrl = "https://avatars.githubusercontent.com/u/71401479?s=48&v=4"
        ),

        Profesor(
            profeId = 4,
            nombreProfe = "Gerardo Tole",
            imageprofeUrl = "https://co.linkedin.com/in/gerardo-tole-galvis-ms-c-94239452",
        ),

        Profesor(
            profeId = 5,
            nombreProfe = "Juan Esteban Rojas",
            imageprofeUrl = "https://media.licdn.com/dms/image/v2/D4D03AQHK2BuppuoQpA/profile-displayphoto-shrink_200_200/B4DZSQH25pHYAY-/0/1737584767975?e=2147483647&v=beta&t=4zzz7DERqawSEqe0PqK99sXECd6PJzhSiHXMlKxLq4Q"
        ),

        Profesor(
            profeId = 6,
            nombreProfe = "Natalia Torres",
            imageprofeUrl = "https://mobileapi.niriun.com/storage/natalia-torres-torres/profile/natalia-torres-torres-niriun-98.webp",
        ),

        Profesor(
            profeId = 7,
            nombreProfe = "Diego Herrera",
            imageprofeUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTtvVgJ9r640mUZ4bAiCL6bguEpU7Td41IneA&s",
        ),

        Profesor(
            profeId = 8,
            nombreProfe = "Camila Castro",
            imageprofeUrl = "data:image/jpeg;base64,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",
        )

    )
}