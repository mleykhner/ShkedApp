package ru.mleykhner.shkedapp.android.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.mleykhner.shkedapp.android.R
import ru.mleykhner.shkedapp.android.ui.theme.AppTheme
import ru.mleykhner.shkedapp.android.ui.theme.errorTextStyle

@Composable
fun AuthSheet(
    //viewModel: AuthViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    var password by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
    ) {
        if (navBackStackEntry?.destination?.hasRoute("emailOrPhone", null) == false) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        } else {
            Box(modifier = Modifier.height(16.dp))
        }
        NavHost(
            navController,
            startDestination = "registrationStart",
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 12.dp)
        ) {
            composable("emailOrPhone") {
                EmailOrPhoneStepPage(
                    emailOrPhone = "",
                    isNextEnabled = true,
                    onChange = {},
                    onNextPressed = { navController.navigate("registrationStart") }
                )
            }
            composable("password") {
                PasswordStepPage(password, {}, "mleykhner",true, {}, false)
            }
            composable("registrationStart") {
                RegistrationStart("mleykhner", onNextPressed = {})
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
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "Давай знакомиться!",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "Чтобы добавлять и просматривать задания нужно авторизоваться",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = emailOrPhone,
                    onValueChange = onChange,
                    label = { Text("Почта", style = MaterialTheme.typography.bodyLarge) },
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
                        "Продолжить",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            Text(
                "Нажимая <<Продолжить>>, Вы принимаете политику конфиденциальности и пользовательское соглашение",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
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
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Последний шаг",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "Осталось только ввести пароль",
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
                        label = { Text("Пароль", style = MaterialTheme.typography.bodyLarge) },
                        textStyle = MaterialTheme.typography.bodyLarge,
                        visualTransformation = if (showPassword)
                            VisualTransformation.None
                        else PasswordVisualTransformation(),
                        isError = isError,
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Crossfade(targetState = showPassword, label = "IconCrossFade") { show ->
                                    if (show) {
                                        Icon(painterResource(id = R.drawable.visibility_off), contentDescription = null)
                                    } else {
                                        Icon(painterResource(id = R.drawable.visibility), contentDescription = null)
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
                            text = "Пароль не подходит",
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
                        "Продолжить",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            Text(
                "Не получается войти?",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun RegistrationStart(emailOrPhone: String, onNextPressed: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Похоже, ты впервые здесь",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "Расскажи немного о себе",
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
                        "Продолжить регистрацию",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp)
                    )
                }
            }
            Text(
                "Нажимая <<Продолжить>>, Вы принимаете политику конфиденциальности и пользовательское соглашение",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(widthDp = 360, showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun AuthSheet_Preview() {
    AppTheme {
        AuthSheet()
    }
}