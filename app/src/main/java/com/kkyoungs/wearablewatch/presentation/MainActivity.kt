/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.kkyoungs.wearablewatch.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(modifier = Modifier.fillMaxSize()) {
                BarcodeView(
                    barcodeValue = "1234567890",
                    barcodeWidth = 200.dp,
                    barcodeHeight = 100.dp
                )
            }
        }
        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {
            setUpBarcodeTranscription()
        }
    }
    private fun setUpBarcodeTranscription(){
        val capabilityInfo : CapabilityInfo = Tasks.await(
            Wearable.getCapabilityClient(this)
                .getCapability(BARCODE_TRANSCRIPTION_CAPABILITY_NAME, CapabilityClient.FILTER_REACHABLE)
        )
        updateTranscriptionCapability(capabilityInfo)
    }

    private var transcriptionNodeId : String ?= null

    private fun updateTranscriptionCapability(capabilityInfo: CapabilityInfo){
        transcriptionNodeId = pickBestNodeId(capabilityInfo.nodes)
        println(">>>>> transcriptionNodeId$transcriptionNodeId")
    }
    private fun pickBestNodeId(nodes : Set<Node>) : String? {
        return nodes.firstOrNull{it.isNearby}?.id?:nodes.firstOrNull()?.id
    }

    private fun requestTranscription(voiceData : ByteArray){
        transcriptionNodeId?.also { nodeID ->
            val sendTask : Task<*> = Wearable.getMessageClient(this).sendMessage(
                nodeID,
                VOICE_TRANSCRIPTION_MESSAGE_PATH,
                voiceData
            ).apply {
                addOnSuccessListener {
                    Toast.makeText(this@MainActivity, "success", Toast.LENGTH_SHORT).show()
                }
                addOnFailureListener {
                    Toast.makeText(this@MainActivity, "failure", Toast.LENGTH_SHORT).show()

                }
            }
            println(">>>> sendTask$sendTask")

        }
    }
    companion object {
        private const val BARCODE_TRANSCRIPTION_CAPABILITY_NAME = "verify_remote_example_phone_app"
        const val VOICE_TRANSCRIPTION_MESSAGE_PATH = "/verify_remote_example_phone_app"
    }

}
