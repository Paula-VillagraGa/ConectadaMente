package com.example.conectadamente.ui.homeUser

/*
@Composable
fun Perfil(patient: PatientModel, onSave: (PatientModel) -> Unit) {
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(patient.name) }
    var email by remember { mutableStateOf(patient.email) }
    var phoneNumber by remember { mutableStateOf(patient.phoneNumber) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)) // Fondo similar a bg-slate-50
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver",
                tint = Color(0xFF0e141b)
            )
            Text(
                text = "Perfil",
                style = TextStyle(
                    fontFamily = PoppinsFontFamily,
                    fontStyle = FontStyle.Italic,
                    fontSize = 26.sp,
                ),
                color = Purple30
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Información del usuario
        if (isEditing) {
            // Dividimos 'name' en dos partes: nombre y apellido
            val nameParts = name.split(" ", limit = 2)
            val firstName = nameParts.getOrElse(0) { "" }
            val lastName = nameParts.getOrElse(1) { "" }

            // Campos de edición
            EditableUserInfoItem(
                icon = Icons.Default.Person,
                label = "Nombre",
                value = firstName,
                onValueChange = { firstName = it }
            )
            EditableUserInfoItem(
                icon = Icons.Default.Person,
                label = "Apellido",
                value = lastName,
                onValueChange = { lastName = it }
            )
            EditableUserInfoItem(
                icon = Icons.Default.Email,
                label = "Correo",
                value = email,
                onValueChange = { email = it }
            )
            EditableUserInfoItem(
                icon = Icons.Default.Call,
                label = "Teléfono",
                value = phoneNumber,
                onValueChange = { phoneNumber = it }
            )
        } else {
            // Si no está en edición, mostrar 'name' como un solo campo
            UserInfoItem(icon = Icons.Default.Person, label = "Nombre: $name")
            UserInfoItem(icon = Icons.Default.Email, label = "Correo: $email")
            UserInfoItem(icon = Icons.Default.Call, label = "Teléfono: $phoneNumber")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botones
        if (isEditing) {
            Button(
                onClick = {
                    val updatedName = "$firstName $lastName"
                    val updatedPatient = patient.copy(
                        name = updatedName,
                        email = email,
                        phoneNumber = phoneNumber
                    )
                    onSave(updatedPatient)
                    isEditing = false // Deshabilitar la edición después de guardar
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple20)
            ) {
                Text(text = "Guardar", color = Color.White)
            }
        } else {
            // Botón para activar la edición
            Button(
                onClick = { isEditing = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple20)
            ) {
                Text(text = "Editar", color = Color.White)
            }
        }
    }
}
}
*/
