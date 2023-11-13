package ru.mleykhner.shkedapp.android.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.icerock.moko.mvvm.flow.compose.collectAsMutableState
import dev.icerock.moko.mvvm.flow.compose.observeAsActions
import dev.icerock.moko.resources.format
import dev.jeziellago.compose.markdowntext.MarkdownText
import ru.mleykhner.shared_resources.SharedRes
import ru.mleykhner.shkedapp.android.R
import ru.mleykhner.shkedapp.android.ui.theme.AppTheme
import ru.mleykhner.shkedapp.android.ui.theme.errorTextStyle
import ru.mleykhner.shkedapp.utils.Strings
import ru.mleykhner.shkedapp.vm.AuthViewModel

@Composable
fun AuthSheet(
    viewModel: AuthViewModel = viewModel(),
    onDismiss: () -> Unit = {}
) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isNextButtonEmailOrPhoneAvailable by viewModel.isNextButtonEmailOrPhoneAvailable.collectAsState()
    val hasError by viewModel.hasError.collectAsState()

    var emailOrPhone by viewModel.emailOrPhone.collectAsMutableState()
    val password by viewModel.password.collectAsState()

    viewModel.actions.observeAsActions { action ->
        when (action) {
            AuthViewModel.Action.LoginSuccess -> onDismiss()
        }
    }

    Surface(
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = Modifier
            .imePadding()
            .fillMaxWidth(),
        shadowElevation = 4.dp,
        tonalElevation = 4.dp
    ) {
        Column {
            Crossfade(
                targetState = (
                        navBackStackEntry?.destination?.hasRoute(
                            AuthStep.EMAIL_OR_PHONE.name, null
                        ) == false
                        ),
                label = "BackButton"
            ) { hidden ->
                when (hidden) {
                    false -> Box(modifier = Modifier.height(16.dp))
                    true -> IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            NavHost(
                navController,
                startDestination = AuthStep.EMAIL_OR_PHONE.name,
                enterTransition = { slideInHorizontally() },
                exitTransition = { fadeOut() }
            ) {
                composable(AuthStep.EMAIL_OR_PHONE.name) {
                    EmailOrPhoneStepPage(
                        emailOrPhone = emailOrPhone,
                        isNextEnabled = isNextButtonEmailOrPhoneAvailable,
                        onChange = { emailOrPhone = it },
                        // TODO: Добавить проверку почты
                        onNextPressed = { navController.navigate(AuthStep.PASSWORD.name) }
                    )
                }
                composable(AuthStep.PASSWORD.name) {
                    PasswordStepPage(
                        password = password,
                        onPasswordChange = viewModel::updatePassword,
                        emailOrPhone = emailOrPhone,
                        isNextEnabled = password.isNotBlank(),
                        onNextPressed = viewModel::signInWithPassword,
                        isError = hasError
                    )
                }
                composable(AuthStep.REG_START.name) {
                    RegistrationStart(emailOrPhone, onNextPressed = {})
                }
            }
        }
    }

}

@Composable
fun EmailOrPhoneStepPage(
    emailOrPhone: String,
    isNextEnabled: Boolean,
    onChange: (String) -> Unit,
    onNextPressed: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
            .padding(horizontal = 20.dp)
            .padding(bottom = 12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                Strings(LocalContext.current)
                    .get(SharedRes.strings.email_or_phone_step_page_heading, emptyList()),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                Strings(LocalContext.current)
                    .get(SharedRes.strings.email_or_phone_step_page_description, emptyList()),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = emailOrPhone,
                    onValueChange = onChange,
                    label = {
                        Text(
                            Strings(LocalContext.current)
                                .get(SharedRes.strings.email_or_phone, emptyList()),
                            style = MaterialTheme.typography.bodyLarge
                        )
                            },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = onNextPressed,
                    enabled = isNextEnabled,
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        Strings(LocalContext.current)
                            .get(SharedRes.strings.continue_button, emptyList()),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            val source = SharedRes.strings.legal_text.format(
                Strings(LocalContext.current)
                    .get(SharedRes.strings.continue_button, emptyList())
            ).toString(LocalContext.current)
            MarkdownText(
                markdown = source,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                linkColor = MaterialTheme.colorScheme.secondary
            )
        }

    }
}

@Composable
fun PasswordStepPage(
    password: String,
    onPasswordChange: (String) -> Unit,
    emailOrPhone: String,
    isNextEnabled: Boolean,
    onNextPressed: () -> Unit,
    isError: Boolean
) {
    var showPassword by remember {
        mutableStateOf(false)
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
            .padding(horizontal = 20.dp)
            .padding(bottom = 12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                Strings(LocalContext.current)
                    .get(SharedRes.strings.password_step_page_heading, emptyList()),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                Strings(LocalContext.current)
                    .get(SharedRes.strings.password_step_page_description, emptyList()),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(
                        Icons.Rounded.AccountCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = emailOrPhone,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    OutlinedTextField(
                        value = password,
                        onValueChange = onPasswordChange,
                        label = {
                            Text(
                                Strings(LocalContext.current)
                                    .get(SharedRes.strings.password, emptyList()),
                                style = MaterialTheme.typography.bodyLarge
                            )
                                },
                        textStyle = MaterialTheme.typography.bodyLarge,
                        visualTransformation = if (showPassword)
                            VisualTransformation.None
                        else PasswordVisualTransformation(),
                        isError = isError,
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Crossfade(
                                    targetState = showPassword,
                                    label = "IconCrossFade"
                                ) { show ->
                                    if (show) {
                                        Icon(
                                            painterResource(id = R.drawable.visibility_off),
                                            contentDescription = Strings(LocalContext.current)
                                                .get(SharedRes.strings.hide_password, emptyList())
                                        )
                                    } else {
                                        Icon(
                                            painterResource(id = R.drawable.visibility),
                                            contentDescription = Strings(LocalContext.current)
                                                .get(SharedRes.strings.show_password, emptyList())
                                        )
                                    }
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    AnimatedVisibility(
                        isError,
                        enter = slideInVertically() + expandVertically() + fadeIn(),
                        exit = slideOutVertically() + shrinkVertically() + fadeOut(),
                        label = "ErrorTextVisibility"
                    ) {
                        Text(
                            text = Strings(LocalContext.current)
                                .get(SharedRes.strings.wrong_password, emptyList()),
                            style = errorTextStyle,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Button(
                    onClick = onNextPressed,
                    enabled = isNextEnabled,
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        Strings(LocalContext.current)
                            .get(SharedRes.strings.continue_button, emptyList()),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            Text(
                Strings(LocalContext.current).get(SharedRes.strings.cant_sign_in, emptyList()),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun RegistrationStart(emailOrPhone: String, onNextPressed: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
            .padding(horizontal = 20.dp)
            .padding(bottom = 12.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                Strings(LocalContext.current)
                    .get(SharedRes.strings.registration_step_page_heading, emptyList()),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                Strings(LocalContext.current)
                    .get(SharedRes.strings.registration_step_page_description, emptyList()),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(
                        Icons.Rounded.AccountCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = emailOrPhone,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Button(
                    onClick = onNextPressed,
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        Strings(LocalContext.current)
                            .get(SharedRes.strings.continue_registration, emptyList()),
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp)
                    )
                }
            }
            val source = SharedRes.strings.legal_text.format(
                Strings(LocalContext.current)
                    .get(SharedRes.strings.continue_registration, emptyList())
            ).toString(LocalContext.current)
            MarkdownText(
                markdown = source,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                linkColor = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

enum class AuthStep {
    EMAIL_OR_PHONE, PASSWORD, PASSKEY, REG_START, REG_NAME, REG_PASSWORD, FINISH
}

@Preview(
    widthDp = 360,
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    locale = "ru"
)
@Composable
fun AuthSheet_Preview() {
    AppTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            AuthSheet()
        }
    }
}