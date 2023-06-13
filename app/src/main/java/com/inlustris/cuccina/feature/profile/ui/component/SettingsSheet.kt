@file:OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)

package com.inlustris.cuccina.feature.profile.ui.component

import ai.atick.material.MaterialColor
import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilustris.cuccina.BuildConfig
import com.ilustris.cuccina.R
import com.inlustris.cuccina.theme.CuccinaLoader
import com.ilustris.cuccina.ui.theme.defaultRadius
import com.ilustris.cuccina.ui.theme.getDeviceMultiplier
import com.inlustris.cuccina.feature.profile.presentation.SettingsViewModel
import com.inlustris.cuccina.theme.annotatedPage
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun SettingsSheet() {

    val settingsViewModel: SettingsViewModel = hiltViewModel()

    val user = settingsViewModel.user.observeAsState().value
    val userInfo = settingsViewModel.userInfo.observeAsState().value
    val userState = settingsViewModel.userState.observeAsState().value
    val viewModelState = settingsViewModel.viewModelState.observeAsState().value
    Box(
        modifier = Modifier.wrapContentSize(),
    ) {

        AnimatedVisibility(visible = user != null, enter = fadeIn(), exit = fadeOut()) {
            val dialogState = remember {
                mutableStateOf(false)
            }
            val context = LocalContext.current

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Configurações",
                    style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.padding(16.dp)
                )

                GlideImage(
                    imageModel = { user?.photoUrl },
                    imageOptions = ImageOptions(
                        requestSize = IntSize(300, 300),
                        contentScale = ContentScale.Fit
                    ),
                    modifier = Modifier
                        .wrapContentSize()
                        .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .clip(CircleShape)
                )

                Text(
                    text = user?.name ?: "",
                    style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                Text(
                    text = userInfo?.email ?: "",
                    style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                userInfo?.providerData?.last()?.run {
                    Text(
                        annotatedPage(
                            "conectado via $providerId.",
                            listOf(providerId),
                            color = MaterialColor.BlueA100
                        ),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            fontWeight = FontWeight.Light
                        ),
                        modifier = Modifier.padding(4.dp)
                    )

                }

                Text(
                    text = userInfo?.uid ?: "",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.2f
                        )
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                )

                Button(
                    onClick = { settingsViewModel.disconnect() },
                    shape = RoundedCornerShape(
                        defaultRadius
                    ),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                        contentColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = ButtonDefaults.elevation(0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Desconectar",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(8.dp)
                            .background(Color.Transparent)
                    )
                }

                Button(
                    onClick = {
                        dialogState.value = true
                    },
                    elevation = ButtonDefaults.elevation(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(defaultRadius)
                ) {
                    Text(
                        text = "Remover dados",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialColor.Red500),
                        modifier = Modifier.padding(8.dp)
                    )
                }


                val appName = context.getString(R.string.app_name)

                Text(
                    text = "Ao utilizar o $appName você concorda com os termos de uso e privacidade.",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable {

                        }
                )

                Text(
                    text = stringResource(id = R.string.version_text),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }


            AnimatedVisibility(
                visible = dialogState.value,
                enter = fadeIn(),
                exit = slideOutVertically()
            ) {
                AlertDialog(modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp)
                    .background(
                        MaterialTheme.colorScheme.surface, RoundedCornerShape(
                            defaultRadius
                        )
                    ),
                    onDismissRequest = {
                        dialogState.value = false
                    }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp * getDeviceMultiplier())
                            .wrapContentHeight(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Tem certeza?",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Remover sua conta irá apagar todos os seus dados do aplicativo. Essa ação não pode ser desfeita.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Button(
                            onClick = { settingsViewModel.removeUserData() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(defaultRadius),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialColor.Red500,
                                contentColor = MaterialColor.White
                            )
                        ) {
                            Text(text = "Excluir")
                        }

                        Button(
                            onClick = {
                                dialogState.value = false
                            },
                            shape = RoundedCornerShape(defaultRadius),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface,
                                backgroundColor = MaterialTheme.colorScheme.surface
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Cancelar"
                            )
                        }

                    }
                }
            }
        }

        AnimatedVisibility(
            visible = viewModelState == ViewModelBaseState.LoadingState || viewModelState == null,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut(),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
        ) {
            CuccinaLoader(showText = false)
        }

        if (userState == SettingsViewModel.UserState.NOT_LOGGED) {
            (LocalContext.current as? Activity)?.run {
                finish()
            }
        }
    }

    LaunchedEffect(Unit) {
        if (user == null) settingsViewModel.getUserData()
    }
}