@file:OptIn(ExperimentalMaterial3Api::class)

package com.inlustris.cuccina.feature.recipe.step.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ilustris.cuccina.R
import com.inlustris.cuccina.theme.annotatedPage

@Composable
fun InstructionItem(
    instruction: String = "",
    savedIngredients: List<String> = emptyList(),
    count: Int,
    editable: Boolean,
    isLastItem: Boolean = false,
    icon: Int? = null,
    iconDescription: String? = "",
    onSelectInstruction: (String) -> Unit,
) {
    val instructionValue = remember {
        mutableStateOf(instruction)
    }


    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp)) {


        val (instructionText, counterText, dividerText) = createRefs()
        val instructionTextModifier = Modifier.constrainAs(instructionText) {
            top.linkTo(counterText.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(counterText.end)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
            height = Dimension.wrapContent
        }.padding(8.dp)
        Text(
            text = (count).toString(),
            modifier = Modifier
                .constrainAs(counterText) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .background(
                    MaterialTheme.colorScheme.onBackground, CircleShape
                )
                .padding(8.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.background,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Black
            ),
        )
        AnimatedVisibility(
            visible = !isLastItem, enter = fadeIn(), exit = fadeOut(),
            modifier = Modifier.constrainAs(dividerText) {
                top.linkTo(counterText.bottom)
                bottom.linkTo(instructionText.bottom)
                start.linkTo(counterText.start, margin = 10.dp)
                end.linkTo(counterText.end, margin = 10.dp)
                height = Dimension.fillToConstraints
            }
        ) {
            Divider(
                modifier = Modifier.width(2.dp),
                color = MaterialTheme.colorScheme.onBackground,
                thickness = 2.dp)

        }

        if (editable) {
            TextField(
                value = instructionValue.value,
                modifier = instructionTextModifier,
                trailingIcon = {
                    if (instructionValue.value.isNotEmpty()) {
                        icon?.let {
                            IconButton(onClick = {
                                onSelectInstruction(instructionValue.value)
                                instructionValue.value = ""
                            }, content = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = icon),
                                    contentDescription = iconDescription
                                )
                            })
                        }
                    }


                },
                placeholder = {
                    Text(
                        text = "Escreva a ${count}º instrução",
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                onValueChange = {
                    instructionValue.value = it
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start
                )
            )
        } else {
            Text(
                text = annotatedPage(
                    instruction,
                    savedIngredients,
                    MaterialTheme.colorScheme.primary
                ),
                modifier = instructionTextModifier
                    .clickable {
                        onSelectInstruction(instructionValue.value)
                    },
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start
                ),
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun InstructionItemPreview() {
    Column {
        listOf(
            "Frite o bacon e reserve em um prato com papel toalha. ",
            "Seque a carne com papel toalha e tempere com sal e pimenta-do-reino. ",
            "Deixe descansar por 10 minutos.",
            "Seque a carne com papel toalha e tempere com sal e pimenta-do-reino. ",
        ).forEachIndexed { index, s ->
            InstructionItem(
                instruction = s,
                savedIngredients = listOf("bacon", "carne", "sal", "pimenta"),
                count = index + 1,
                editable = index % 2 == 0,
                isLastItem = index == 3,
                icon = R.drawable.iconmonstr_line_one_horizontal_lined,
            ) {

            }
        }
    }
}
