package com.example.budgetcontrolandroid.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.budgetcontrolandroid.presentation.theme.AppColors
import com.example.budgetcontrolandroid.presentation.theme.AppTypography

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester? = null,
    hintText: String? = null,
    labelText: String? = null,
    errorText: String? = null,
    color: Color = AppColors.onSecondary,
    onComplete: () -> Unit = {},
    onTap: () -> Unit = {},
    maxLines: Int = 1,
    readOnly: Boolean = false,
    enableFocus: Boolean = false,
    suffix: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    width: Dp? = null,
    height: Dp? = null,
    radius: Double = 6.0,
    padding: Double? = null,
    colorBorder: Color,
    textLength: Int? = null,
    textInputFormatter: VisualTransformation? = null,
    obscureText: Boolean? = false,
    textInputAction: ImeAction? = null,
    textInputType: KeyboardType? = null,
    modifier: Modifier = Modifier,
    hintStyle: TextStyle? = null
) {
    var text = value
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }


    Column(
        modifier = modifier.padding(
            horizontal = 16.dp,
            vertical = (padding ?: 0.0).dp
        ),
        horizontalAlignment = Alignment.Start
    ) {
        BasicTextField(
            value = text,
            onValueChange = {
                if (textLength == null || it.length <= textLength) {
                    text = it
                    onValueChange(it)
                }
            },
            modifier =

                Modifier
                    .wrapContentSize()
                    .then(if (width != null) Modifier.width(width) else Modifier.fillMaxWidth())
                    .height(height ?: (maxLines * 24 + 20).dp)

                    .background(color, shape = RoundedCornerShape(radius.dp))
                    .border(1.dp, colorBorder, RoundedCornerShape(radius.dp))
                    .shadow(
                        2.dp,
                        RoundedCornerShape(radius.dp),
                        ambientColor = AppColors.onSecondary.copy(alpha = 0.8f),
                        spotColor = AppColors.onSecondary.copy(alpha = 0.8f)
                    )
                    .padding(
                        horizontal = 12.dp,
                        vertical = (padding ?: 0.0).dp
                    )
                    .focusRequester(focusRequester ?: FocusRequester())
                    .then(if (enableFocus) Modifier.clickable { onTap() } else Modifier),
            enabled = !readOnly,
            textStyle = AppTypography.textTheme.titleMedium.copy(color = AppColors.secondary),
            maxLines = maxLines,
            singleLine = maxLines == 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = textInputType ?: KeyboardType.Text,
                imeAction = textInputAction ?: ImeAction.Default
            ),
            keyboardActions = KeyboardActions(
                onDone = { onComplete(); focusManager.clearFocus() }
            ),
            visualTransformation = if (obscureText == true) PasswordVisualTransformation() else textInputFormatter
                ?: VisualTransformation.None,
            decorationBox = { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    innerTextField = innerTextField,
                    value = value,
                    enabled = !readOnly,
                    singleLine = maxLines == 1,
                    visualTransformation = if (obscureText == true) PasswordVisualTransformation() else textInputFormatter
                        ?: VisualTransformation.None,
                    interactionSource = interactionSource,
                    contentPadding = PaddingValues(0.dp),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (prefix != null) {
                        Box(modifier = Modifier.padding(end = 8.dp)) {
                            prefix()
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 2.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        innerTextField()
                        if (hintText != null && text.isEmpty()) {
                            Text(
                                text = hintText,
                                style = hintStyle ?: TextStyle(
                                    color = AppColors.primary.copy(alpha = 0.8f),
                                    fontSize = 14.sp
                                ),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                    if (suffix != null) {
                        Box(modifier = Modifier.padding(start = 8.dp)) {
                            suffix()
                        }
                    }
                }
            }
        )
        if (errorText != null) {
            Text(
                text = errorText,
                style = AppTypography.textTheme.bodySmall.copy(color = AppColors.error),
            )
        }
    }
}

@Preview
@Composable
private fun AppTextFieldPreview() {
    AppTextField(
        value = "",
        onValueChange = {},
        colorBorder = AppColors.primary,
        hintText = "Login"
    )
}