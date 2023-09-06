package ru.mleykhner.shkedapp.android.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mleykhner.shared_resources.SharedRes
import ru.mleykhner.shkedapp.android.R
import ru.mleykhner.shkedapp.android.ui.theme.AppTheme
import ru.mleykhner.shkedapp.utils.Strings

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AuthScreen(
    isPresented: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {

    val (emailFieldFocus, passwordFieldFocus) = FocusRequester.createRefs()
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var showPassword by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(isPresented) {
        if (isPresented)
            emailFieldFocus.requestFocus()
    }


    if (isPresented)
        ModalBottomSheet(
            onDismissRequest = {
                onDismiss()
                               },
            modifier = modifier,
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .padding(bottom = 18.dp)
                    .fillMaxWidth()
            ) {
                val focusManager = LocalFocusManager.current

                Text(
                    text = Strings(LocalContext.current)
                        .get(SharedRes.strings.auth_screen_heading, emptyList()),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = Strings(LocalContext.current)
                        .get(SharedRes.strings.auth_screen_description, emptyList()),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(18.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = {
                        Text(
                            Strings(LocalContext.current)
                                .get(SharedRes.strings.email, emptyList())
                        )
                            },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions{
                            focusManager.moveFocus(FocusDirection.Next)
                        },
                    modifier = Modifier
                        .focusRequester(emailFieldFocus)
                        .focusProperties {
                            next = passwordFieldFocus
                        }
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = {
                        Text(
                            Strings(LocalContext.current)
                                .get(SharedRes.strings.password, emptyList())
                            )
                            },
                    visualTransformation = if (showPassword)
                        VisualTransformation.None else
                            PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            onClick = { showPassword = !showPassword }
                        ) {
                            AnimatedContent(
                                targetState = showPassword,
                                label = "VisibilityTransition"
                            ) {animationState ->
                                Icon(
                                    painterResource(
                                        if (animationState)
                                            R.drawable.visibility_off else R.drawable.visibility
                                    ),
                                    if (animationState)
                                        Strings(LocalContext.current)
                                            .get(SharedRes.strings.hide_password, emptyList()) else
                                        Strings(LocalContext.current)
                                            .get(SharedRes.strings.show_password, emptyList())
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions{
                        Log.i("Keyboard Action", "Registering")
                    },
                    modifier = Modifier
                        .focusRequester(passwordFieldFocus)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                    .fillMaxWidth()
                ) {
                    Text(
                        Strings(LocalContext.current)
                            .get(SharedRes.strings.sign_in_verb, emptyList())
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                TextButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                    .fillMaxWidth()
                ) {
                    Text(
                        Strings(LocalContext.current)
                            .get(SharedRes.strings.sign_up_noun, emptyList())
                    )
                }
            }
        }
}

@Preview(widthDp = 360)
@Composable
fun AuthScreen_Preview() {
    var presented by remember {
        mutableStateOf(false)
    }
    AppTheme {
        Box {
            Button(onClick = { presented = true }) {
                Text(text = "show")
            }
            AuthScreen(isPresented = presented, onDismiss = { presented = false })
        }
    }
}