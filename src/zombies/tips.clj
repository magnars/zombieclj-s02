(ns zombies.tips)

(def tips
  [{:id :zombies-intro
    :position :at-zombies
    :header "Zombiene kommer!"
    :prose "Det er zombier overalt. En av dem har oppdaget deg. Du har også sett tre andre i nærheten."}

   {:id :player-health-intro
    :position :at-player-health
    :header "Dine helsepoeng"
    :prose "Disse små hjertene er alt som står mellom deg og de vandøde."}

   ;; mer intro:

   {:id :dice-intro
    :position :at-dice
    :header "Gjør slutt på zombiene, før de gjør slutt på deg"
    :prose "Slå dem med knyttnever, beskytt deg med skjold. Hjertene gir helsepoeng tilbake, spader lar deg grave etter noe nyttig på søppeldynga. Unngå skallene. De gjør ingenting."}

   {:id :reroll-intro
    :position :at-rerolls
    :header "Du har to omslag"
    :prose "Lås terningene du vil beholde, og trykk på disse for å kaste terningene på nytt."}

   {:id :intentions-intro
    :position :at-intentions
    :header "Zombier er lette å gjennomskue"
    :prose "Du kan se hva zombiene har tenkt å gjøre på sin tur. Bruk dette til å forberede deg."}

   ;; tips kan avhenge av game-state:

   {:id :punching-a-zombie
    :position :at-zombies
    :header "På tide å denge løs"
    :prose "Klikk på zombien for å angripe den."}

   {:id :not-punching-a-zombie
    :position :at-zombies
    :header "Vold er ikke alltid løsningen"
    :prose "Selv om du ikke har noen knyttnever, så må du trykke på en zombie for å avslutte turen din."}

   ;; misc:

   {:id :player-shields-intro
    :position :at-player-shields
    :header "Forsvar er det beste forsvar"
    :prose "Det er mer effektivt å blokkere zombienes slag enn å helbrede, men skjoldene varer bare denne runden."}

   {:id :butcher-boss-fight
    :position :at-zombies
    :header "Skitt pomfritt!"
    :prose "Den siste zombien er mye tøffere enn resten. Se opp for slakterkniven!"}

   ])
