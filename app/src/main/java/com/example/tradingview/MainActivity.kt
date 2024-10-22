package com.example.tradingview

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.tradingview.ui.theme.TradingViewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TradingViewTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column (modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 17.dp)
                        .verticalScroll(rememberScrollState())
                    ){
                        MyWebViewScreen(symbol = "FOREXCOM:AUDUSD")
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(onClick = { }) {
                                Text(text = "BUY")
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Button(onClick = {  }) {
                                Text(text = "SELL")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyWebViewScreen(symbol: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WebViewTest(
            symbol = symbol,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun WebViewTest(modifier: Modifier = Modifier, symbol: String) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                }
                webViewClient = WebViewClient()

                loadDataWithBaseURL(
                    null,
                    getTradingViewHtml(symbol),
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        }
    )
}

fun getTradingViewHtml(symbol: String): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>
        <body>
            <!-- TradingView Widget BEGIN -->
            <div class="tradingview-widget-container">
              <div id="tradingview_chart"></div>
              <script type="text/javascript" src="https://s3.tradingview.com/tv.js"></script>
              <script type="text/javascript">
              new TradingView.widget(
              {
              "width": "100%",
              "height": 500,
              "symbol": "$symbol",
              "interval": "D",
              "timezone": "Etc/UTC",
              "theme": "light",
              "style": "1",
              "locale": "en",
              "toolbar_bg": "#f1f3f6",
              "enable_publishing": false,
              "allow_symbol_change": false,
              "container_id": "tradingview_chart"
              }
              );
              </script>
            </div>
            <!-- TradingView Widget END -->
        </body>
        </html>
    """.trimIndent().replace("\$symbol", symbol) // Replace placeholder with actual symbol
}

