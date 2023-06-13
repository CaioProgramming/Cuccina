package com.inlustris.cuccina.navigation

import ai.atick.material.MaterialColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.ilustris.cuccina.R
import com.inlustris.cuccina.feature.home.ui.HOME_ROUTE
import com.inlustris.cuccina.feature.home.ui.HomeView
import com.inlustris.cuccina.feature.profile.ui.PROFILE_ROUTE
import com.inlustris.cuccina.feature.profile.ui.ProfileView
import com.inlustris.cuccina.feature.recipe.form.ui.NEW_RECIPE_ROUTE
import com.inlustris.cuccina.feature.recipe.form.ui.NewRecipeView
import com.inlustris.cuccina.feature.recipe.start.ui.START_RECIPE_ROUTE
import com.inlustris.cuccina.feature.recipe.start.ui.StartRecipeView
import com.inlustris.cuccina.feature.recipe.category.ui.CATEGORY_ROUTE
import com.inlustris.cuccina.feature.recipe.category.ui.CategoryView

enum class BottomNavItem(
    val title: String,
    var icon: Int = R.drawable.cherry,
    val route: String,
    val showOnNavigation: Boolean = true,
    val showBottomNav: Boolean = true,
    val showStatusBar: Boolean = true
) {
    HOME(title = "Home", route = HOME_ROUTE, icon = R.drawable.round_home_24),
    NEW_RECIPE(title = "Publicar", route = NEW_RECIPE_ROUTE, icon = R.drawable.cook),
    PROFILE(title = "Eu", route = PROFILE_ROUTE, icon = R.drawable.ic_cherries, showBottomNav = false, showStatusBar = false),
    START_RECIPE(title = "Iniciar Receita", route = START_RECIPE_ROUTE, showOnNavigation = false, showBottomNav = false, showStatusBar = false),
    CATEGORY(title = "Categorias", route = CATEGORY_ROUTE, showOnNavigation = false, showStatusBar = false)
}

@Composable
fun NavigationGraph(navController: NavHostController, bottomPadding: Dp) {

    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE,
        modifier = Modifier.padding(bottom = bottomPadding)
    ) {
        composable(route = BottomNavItem.HOME.route) {
            HomeView(hiltViewModel(), navController)
        }

        composable(route = BottomNavItem.NEW_RECIPE.route) {
            NewRecipeView(hiltViewModel(), navController)
        }

        composable(
            route = BottomNavItem.CATEGORY.route,
            arguments = listOf(navArgument("categoryId") { type = NavType.IntType })
        ) {
            val categoryId = it.arguments?.getInt("categoryId")
            CategoryView(categoryId ?: 0, hiltViewModel(), navController)
        }

        composable(
            route = BottomNavItem.PROFILE.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            val userId = it.arguments?.getString("userId")
            ProfileView(userId, hiltViewModel(), navController)
        }

        composable(
            route = BottomNavItem.START_RECIPE.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
        ) {
            val recipeId = it.arguments?.getString("recipeId")
            StartRecipeView(recipeId, hiltViewModel(), navController)
        }


    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    androidx.compose.material.BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) {
        val routes = BottomNavItem.values().filter { it.showOnNavigation }
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination
        routes.forEach { item ->
            val isSelected = currentRoute?.hierarchy?.any { it.route == item.route } == true
            val itemColor =
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.5f
                )
            BottomNavigationItem(
                selected = isSelected,
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelSmall,
                        color = itemColor
                    )
                },
                icon = {
                    Image(
                        painterResource(item.icon),
                        contentDescription = item.title,
                        modifier = if (item == BottomNavItem.NEW_RECIPE) Modifier
                            .size(24.dp)
                            .background(color = itemColor, shape = CircleShape)
                            .padding(4.dp) else Modifier
                            .size(24.dp)
                            .clip(CircleShape),
                        colorFilter = if (item == BottomNavItem.NEW_RECIPE) ColorFilter.tint(
                            MaterialTheme.colorScheme.background
                        ) else ColorFilter.tint(itemColor)
                    )
                },
                selectedContentColor = MaterialColor.White,
                unselectedContentColor = MaterialColor.White.copy(alpha = 0.3f),
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
    }
}
