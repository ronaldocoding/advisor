package br.com.advisor.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import br.com.advisor.R
import br.com.advisor.presentation.action.MainAction
import br.com.advisor.presentation.state.MainUiState
import br.com.advisor.presentation.viewmodel.MainViewModel
import br.com.advisor.ui.theme.AdvisorTheme
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val UPDATE_CODE = 123

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    private lateinit var appUpdateManager: AppUpdateManager

    private val updateType = AppUpdateType.IMMEDIATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        installSplashScreen()
        checkForAppUpdates()
        setContent {
            AdvisorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AdviceScreen(viewModel = viewModel)
                }
            }
        }
    }

    private fun checkForAppUpdates() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed = info.isImmediateUpdateAllowed
            if ( isUpdateAvailable && isUpdateAllowed) {
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    updateType,
                    this,
                    UPDATE_CODE
                )
            }
        }
    }
}

@Composable
fun AdviceScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.observeAsState()
    Screen(
        state = checkNotNull(state),
        onClickGetAdvice = {
            viewModel.sendAction(MainAction.Action.OnClickGetAdviceButton)
            viewModel.sendAction(MainAction.Action.OnGetAdvice)
        }
    )
}

@Composable
fun Screen(state: MainUiState, onClickGetAdvice: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        when (state) {
            is MainUiState.Initial -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painterResource(id = R.drawable.ic_advice),
                        contentDescription = stringResource(id = R.string.advice_icon),
                        Modifier.padding(top = 48.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.displayLarge,
                        color = Color(0xFF49454F)
                    )
                }
                Text(
                    text = stringResource(state.uiModel.supportingTextResource!!),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF49454F),
                    modifier = Modifier.padding(horizontal = 48.dp),
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = onClickGetAdvice,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 48.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.advice_button),
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                }
            }

            is MainUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            is MainUiState.Advice -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painterResource(id = R.drawable.ic_advice),
                        contentDescription = stringResource(id = R.string.advice_icon),
                        Modifier.padding(top = 48.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.displayLarge,
                        color = Color(0xFF49454F)
                    )
                }
                Text(
                    text = "\"${state.uiModel.adviceText!!}\"",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color(0xFF49454F),
                    modifier = Modifier.padding(horizontal = 48.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(state.uiModel.supportingTextResource!!),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF49454F),
                    modifier = Modifier.padding(horizontal = 48.dp),
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = onClickGetAdvice,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 48.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.advice_button),
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                }
            }

            is MainUiState.Error -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painterResource(id = R.drawable.ic_advice),
                        contentDescription = stringResource(id = R.string.advice_icon),
                        Modifier.padding(top = 48.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.displayLarge,
                        color = Color(0xFF49454F)
                    )
                }
                Text(
                    text = stringResource(state.uiModel.supportingTextResource!!),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF49454F),
                    modifier = Modifier.padding(horizontal = 48.dp),
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = onClickGetAdvice,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 48.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.advice_button),
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                }
            }
        }
    }
}
