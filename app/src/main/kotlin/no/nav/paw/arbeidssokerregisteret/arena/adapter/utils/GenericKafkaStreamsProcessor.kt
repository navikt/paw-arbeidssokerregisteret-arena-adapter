package no.nav.paw.arbeidssokerregisteret.arena.adapter.utils


import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Named
import org.apache.kafka.streams.processor.api.Processor
import org.apache.kafka.streams.processor.api.ProcessorContext
import org.apache.kafka.streams.processor.api.Record

fun <K, V> KStream<K, V>.filter(
    name: String,
    vararg stateStoreNames: String,
    function: (Record<K, V>) -> Boolean
): KStream<K, V> {
    val processor = {
        GenericProcessor<K, V, V> { record ->
            val result = function(record)
            if (result) forward(record)
        }
    }
    return process(processor, Named.`as`(name), *stateStoreNames)
}

fun <K, V> KStream<K, V>.genericProcess(
    name: String,
    vararg stateStoreNames: String,
    function: ProcessorContext<K, V>.(Record<K, V>) -> Unit
): KStream<K, V> {
    val processor = {
        GenericProcessor(function)
    }
    return process(processor, Named.`as`(name), *stateStoreNames)
}

class GenericProcessor<K, V_IN, V_OUT>(
    private val function: ProcessorContext<K, V_OUT>.(Record<K, V_IN>) -> Unit
) : Processor<K, V_IN, K, V_OUT> {
    private var context: ProcessorContext<K, V_OUT>? = null

    override fun init(context: ProcessorContext<K, V_OUT>?) {
        super.init(context)
        this.context = context
    }

    override fun process(record: Record<K, V_IN>?) {
        if (record == null) return
        val ctx = requireNotNull(context) { "Context is not initialized" }
        with(ctx) { function(record) }
    }
}
