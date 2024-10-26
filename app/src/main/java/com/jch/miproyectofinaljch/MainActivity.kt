package com.jch.miproyectofinaljch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jch.miproyectofinaljch.ui.theme.MIproyectofinalJCHTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkMode by remember { mutableStateOf(true) }
            MIproyectofinalJCHTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent(isDarkMode, onToggleTheme = { isDarkMode = !isDarkMode })
                }
            }
        }
    }
}

@Composable
fun MainContent(isDarkMode: Boolean, onToggleTheme: () -> Unit) {
    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2000) // Muestra la pantalla de inicio por 2 segundos
        showSplash = false
    }

    if (showSplash) {
        SplashScreen()
    } else {
        TodoApp(isDarkMode = isDarkMode, onToggleTheme = onToggleTheme)
    }
}

@Composable
fun SplashScreen() {
    val scale = remember { Animatable(0.8f) }

    // Animación de escalado
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(scaleX = scale.value, scaleY = scale.value)
    ) {
        Image(
            painter = painterResource(id = R.drawable.todo_image),
            contentDescription = "Logo de la aplicación",
            modifier = Modifier.size(150.dp)
        )
    }
}

data class TodoItem(val text: String, var isCompleted: Boolean = false)

@Composable
fun TodoApp(
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit
) {
    var todoText by remember { mutableStateOf("") }
    var todoList by remember { mutableStateOf(listOf<TodoItem>()) }
    var showCompletedOnly by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var lastDeletedTask by remember { mutableStateOf<TodoItem?>(null) }

    // Función para agregar una tarea
    fun addTodo() {
        if (todoText.isNotBlank()) {
            todoList = todoList + TodoItem(todoText)
            todoText = ""
        }
    }

    // Función para manejar la eliminación de una tarea con opción de deshacer
    fun deleteTaskWithUndo(task: TodoItem) {
        todoList = todoList - task
        lastDeletedTask = task
        coroutineScope.launch {
            val result = snackbarHostState.showSnackbar(
                message = "Tarea eliminada",
                actionLabel = "Deshacer"
            )
            if (result == SnackbarResult.ActionPerformed) {
                lastDeletedTask?.let { todoList = todoList + it }
                lastDeletedTask = null
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título de la aplicación
                Text(
                    text = "Mis Pendientes",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                // Imagen representativa de la aplicación de tareas
                Image(
                    painter = painterResource(id = R.drawable.todo_image),
                    contentDescription = "Imagen de lista de tareas",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(vertical = 8.dp)
                )

                // Switch para mostrar solo completadas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Mostrar solo completadas", style = MaterialTheme.typography.bodyLarge)
                    CustomSwitch(
                        checked = showCompletedOnly,
                        onCheckedChange = { showCompletedOnly = it }
                    )
                }

                // Campo de entrada para la tarea
                OutlinedTextField(
                    value = todoText,
                    onValueChange = { todoText = it },
                    label = { Text("Nueva tarea") },
                    trailingIcon = {
                        IconButton(onClick = { addTodo() }) {
                            Icon(Icons.Filled.Add, contentDescription = "Agregar tarea")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { addTodo() }
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Lista de tareas
                val filteredTodoList = if (showCompletedOnly) {
                    todoList.filter { it.isCompleted }
                } else {
                    todoList
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    for (task in filteredTodoList) {
                        TodoItemView(
                            task = task,
                            onComplete = { isCompleted ->
                                todoList = todoList.toMutableList().also {
                                    val index = it.indexOf(task)
                                    it[index] = it[index].copy(isCompleted = isCompleted)
                                }
                            },
                            onDelete = {
                                deleteTaskWithUndo(task)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TodoItemView(
    task: TodoItem,
    onComplete: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    val completeColor by animateColorAsState(
        targetValue = if (task.isCompleted) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground
    )
    val textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Checkbox para completar la tarea
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { isChecked -> onComplete(isChecked) },
                modifier = Modifier.padding(end = 8.dp)
            )

            // Texto de la tarea
            Text(
                text = task.text,
                color = completeColor,
                fontSize = 18.sp,
                textDecoration = textDecoration,
                modifier = Modifier.weight(1f)
            )

            // Botón para eliminar la tarea
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val trackColor by animateColorAsState(
        targetValue = if (checked) MaterialTheme.colorScheme.primary else Color.Gray
    )
    val thumbColor by animateColorAsState(
        targetValue = if (checked) MaterialTheme.colorScheme.onPrimary else Color.LightGray
    )

    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedTrackColor = trackColor,
            uncheckedTrackColor = trackColor,
            checkedThumbColor = thumbColor,
            uncheckedThumbColor = thumbColor
        ),
        modifier = Modifier
            .size(50.dp, 25.dp)
            .padding(4.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun TodoAppPreview() {
    MIproyectofinalJCHTheme {
        TodoApp(isDarkMode = true, onToggleTheme = {})
    }
}
