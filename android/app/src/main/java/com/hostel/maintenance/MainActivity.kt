package com.hostel.maintenance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hostel.maintenance.ui.screens.FeeRemindersScreen
import com.hostel.maintenance.ui.screens.MaintenanceScreen
import com.hostel.maintenance.ui.screens.StudentsScreen
import com.hostel.maintenance.ui.theme.HostelTheme
import com.hostel.maintenance.viewmodel.HostelViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as HostelApp
        val factory = HostelViewModel.Factory(app.repository)

        setContent {
            HostelTheme {
                HostelAppRoot(factory)
            }
        }
    }
}

@Composable
private fun HostelAppRoot(factory: HostelViewModel.Factory) {
    val viewModel: HostelViewModel = viewModel(factory = factory)
    val students by viewModel.students.collectAsState()
    val requests by viewModel.maintenanceRequests.collectAsState()
    val reminders by viewModel.feeReminders.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf("Maintenance", "Students", "Fees")

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Build, contentDescription = null) },
                    label = { Text(tabs[0]) },
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text(tabs[1]) },
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
                    label = { Text(tabs[2]) },
                )
            }
        },
    ) { padding ->
        when (selectedTab) {
            0 -> MaintenanceScreen(viewModel, requests, Modifier.padding(padding))
            1 -> StudentsScreen(viewModel, students, Modifier.padding(padding))
            else -> FeeRemindersScreen(viewModel, reminders, Modifier.padding(padding))
        }
    }
}
