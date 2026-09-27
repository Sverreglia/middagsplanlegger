package com.example.demo


import org.springframework.stereotype.Controller
import org.springframework.ui.Model

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

data class Middag(
    val navn: String,
    val kategori: String,
    val emoji: String
    
);

private val middager = mutableListOf<Middag>(
    Middag("Brun saus", "Hverdagsmat", "🐟"),
    Middag("Taco med avocado", "Helgemat", "🍗"),
    Middag("Spagehtti", "Hverdagsmat", "🍝"),
    Middag("Pizzzza", "Helgemat", "🍕"),
    Middag("Burger", "Helgemat", "🍔"),
    Middag("Lasagna", "Helgemat", "🍲"),
    Middag("Fisk", "Helgemat", "🍲")
)
private val ukesMiddager = mutableListOf<Middag>(
    Middag("Brun saus", "Hverdagsmat", "🐟"),
    Middag("Taco med avocado", "Helgemat", "🍗"),
    Middag("Spagehtti", "Hverdagsmat", "🍝"),
    Middag("Pizzzza", "Helgemat", "🍕"),
    Middag("Burger", "Helgemat", "🍔"),
    Middag("Lasagna", "Helgemat", "🍲")
)

private val tilfeldigMiddag = mutableListOf<Middag>()

private val ukedager = listOf(
    "Mandag",
    "Tirsdag",
    "Onsdag",
    "Torsdag",
    "Fredag",
    "Lørdag",
    "Søndag"
)


fun genererTilfeldigMiddag() : List<Middag> {

        //Sletter alt som er i nåværende liste
        tilfeldigMiddag.clear()
        
        //Kjører loopen helt til lista inneholder 7 unike matretter
        while (tilfeldigMiddag.size < 7) {

            //Velger et tall fra 0 til lengden på middager lista
            val index = (0 until middager.size).random()
            
            val middag = middager[index]


            //Her sjekkes først om valgt rett allerede finnes i lista. Bruker any metode, som sjekker alle objekter i lista og sammenligner hvert objekt sitt navn property
            if (!tilfeldigMiddag.any { it.navn == middag.navn }) {
                tilfeldigMiddag.add(middag)
            }
            
        }

        return tilfeldigMiddag
    }


@Controller
class SideController {

    @GetMapping("/")
        fun hjem(model: Model): String {
            
            model.addAttribute("ukesMiddager", genererTilfeldigMiddag())
            model.addAttribute("ukedager", ukedager)
            return "index"

    }

    @GetMapping("/middager")
        fun VisMiddager(model: Model): String {
            model.addAttribute("middager", middager)
            return "middager"
        }

    @GetMapping("/om")
    fun om(): String {
        return "om"
    }

}

@Controller
class MiddagController {

    @PostMapping("/middager")
    fun mottaData(
        @RequestParam navn: String,
        @RequestParam kategori: String,
         @RequestParam emoji: String,

    ): String {
        val nyMiddag = Middag(
            navn = navn,
            kategori = kategori,
            emoji = emoji,
        )

        middager.add(nyMiddag)

        return "redirect:/middager"
    }

    @GetMapping("/middager/delete/{navn}")
        fun slettMiddag(@PathVariable navn: String): String {
            middager.removeIf {it.navn == navn} 

            return "redirect:/middager"
        }

    
}   

@RestController
class MiddagApiController {

    //API som generer en ny tilfeldig rekkefølge av matretter, og returnerer lista i JSON-format
    @GetMapping("/api/random")
    fun hentTilfeldigeMiddager(): List<Middag> {
        return genererTilfeldigMiddag()
    }

    @GetMapping("/api/middager")
    fun hentMiddager(): List<Middag> {
        return middager
    }
        
}

