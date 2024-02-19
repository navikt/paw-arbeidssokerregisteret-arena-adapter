package no.nav.paw.arbeidssokerregisteret.arena.adapter.utils

import no.nav.paw.arbeidssokerregisteret.arena.helpers.v3.TopicsJoin
import java.time.Duration
import java.time.Instant

fun TopicsJoin.skalSlettes(
    gjeldeneTid: Instant,
    tidsfristVentePaaOpplysninger: Duration = Duration.ofHours(1),
    tidsfristVentePaaPeriode: Duration = Duration.ofHours(1),
    tidsfristVentePaaProfilering: Duration = Duration.ofDays(7)
): Boolean = when {
        isEmpty -> true
        harBarePeriode -> avsluttetFoer(gjeldeneTid - tidsfristVentePaaOpplysninger)
        harBareProfilering -> true
        manglerBareProfilering -> opplysningerMottattFoer(gjeldeneTid - tidsfristVentePaaProfilering)
        harBareOpplysninger -> opplysningerMottattFoer(gjeldeneTid - tidsfristVentePaaPeriode)
        manglerBarePeriode -> opplysningerMottattFoer(gjeldeneTid - tidsfristVentePaaPeriode)
        else -> true
    }

fun TopicsJoin.opplysningerMottattFoer(time: Instant): Boolean = opplysningerOmArbeidssoeker?.sendtInnAv?.tidspunkt?.isBefore(time) ?: false
fun TopicsJoin.avsluttetFoer(time: Instant): Boolean = periode?.avsluttet?.tidspunkt?.isBefore(time) ?: false
val TopicsJoin.isEmpty get() = periode == null && profilering == null && opplysningerOmArbeidssoeker == null

val TopicsJoin.harBarePeriode get() = periode != null && profilering == null && opplysningerOmArbeidssoeker == null

val TopicsJoin.harBareProfilering get() = periode == null && profilering != null && opplysningerOmArbeidssoeker == null

val TopicsJoin.manglerBareProfilering get() = periode != null && profilering == null && opplysningerOmArbeidssoeker != null

val TopicsJoin.manglerBarePeriode get() = periode == null && profilering != null && opplysningerOmArbeidssoeker != null

val TopicsJoin.harBareOpplysninger get() = periode == null && profilering == null && opplysningerOmArbeidssoeker != null