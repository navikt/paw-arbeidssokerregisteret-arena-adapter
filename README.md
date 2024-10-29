> [!WARNING]
> Koden er flyttet til [paw-arbeidssoekerregisteret-monorepo-ekstern](https://github.com/navikt/paw-arbeidssoekerregisteret-monorepo-ekstern/tree/main/apps/arena-adapter)

# paw-arbeidssokerregisteret-arena-adapter

Mottak av hendelser (start, stopp, opplysninger om arbeidssøker og profilering) fra arbeidssøkerregisteret, vil bli samlet i en melding.

Denne meldingen vil deretter bli sendt videre til en Kafka-topic som Arena kan lese.
