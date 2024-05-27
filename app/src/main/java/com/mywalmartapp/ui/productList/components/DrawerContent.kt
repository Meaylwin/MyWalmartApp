package com.mywalmartapp.ui.productList.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mywalmartapp.ui.theme.BlueInk
import com.mywalmartapp.ui.theme.WalmartBlue
import com.mywalmartapp.ui.theme.WalmartYellow

@Composable
fun DrawerContent(
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = BlueInk,
        drawerContentColor = WalmartYellow
    ) {
        LazyColumn(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "My Super App",
                        fontSize = 24.sp
                    )
                    Text(
                        text = "Categorías"
                    )
                    Spacer(modifier = Modifier.padding(bottom = 10.dp))
                }
            }
            items(categories) { category ->
                Box(
                    modifier = Modifier.fillMaxSize(0.7F)
                        .clickable { onCategorySelected(category) }
                        .background(
                            color = if (category == selectedCategory) {
                                WalmartBlue
                            } else {
                                Color.Transparent
                            },
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Text(
                        text = category,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 20.sp,
                        color = WalmartYellow
                    )
                }
            }
        }
    }
}