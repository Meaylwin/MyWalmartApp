package com.mywalmartapp.ui.productList

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myproductswallmart.R
import com.mywalmartapp.ui.productList.entities.ProductItem
import com.mywalmartapp.ui.productList.components.BottomSheetContent
import com.mywalmartapp.ui.productList.components.CartSheetContent
import com.mywalmartapp.ui.productList.components.ProductItemComponent
import com.mywalmartapp.ui.theme.BlueInk
import com.mywalmartapp.ui.theme.WalmartBlue
import com.mywalmartapp.ui.theme.WalmartYellow
import com.mywalmartapp.ui.productList.components.DrawerContent
import com.mywalmartapp.ui.productList.components.TopProductComponent
import com.mywalmartapp.ui.productList.entities.SheetContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ProductListScreen(
    productListViewModel: ProductListViewModel = hiltViewModel()
) {
    val topProduct by productListViewModel.topProduct.collectAsState()
    val products by productListViewModel.products.collectAsState()
    val categories by productListViewModel.categories.collectAsState()
    val selectedCategory by productListViewModel.selectedCategory.collectAsState()
    val cartItems by productListViewModel.cartItems.collectAsState()
    val cartTotal by productListViewModel.cartTotal.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val (selectedProduct, setSelectedProduct) = remember { mutableStateOf<ProductItem?>(null) }
    val (sheetContent, setSheetContent) = remember { mutableStateOf(SheetContent.CART) }
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { true },
        skipHalfExpanded = true
    )

    // When using a Column and an infinite LazyVerticalGrid, you may encounter the error:
    // "Vertically scrollable component was measured with an infinity maximum height constraints, which is disallowed"
    // To resolve this, provide a fixed height to the LazyVerticalGrid based on the number of items.
    val gridHeight = 280.dp + (165.dp * (products.size / 2))

    BackHandler(bottomSheetState.isVisible) {
        coroutineScope.launch { bottomSheetState.hide() }
    }

    BackHandler(drawerState.isOpen) {
        coroutineScope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                categories = categories,
                selectedCategory = selectedCategory
            ) { category ->
                productListViewModel.selectCategory(category)
                coroutineScope.launch {
                    drawerState.close()
                }
            }
        }
    ) {
        ModalBottomSheetLayout(
            sheetContent = {
                when (sheetContent) {
                    SheetContent.CART -> {
                        CartSheetContent(
                            cartItems = cartItems,
                            cartTotal = cartTotal,
                            onClickAddProduct = { productItem ->
                                productListViewModel.addToCart(
                                    productItem
                                )
                            },
                            onClickDecreaseProduct = { productItem ->
                                productListViewModel.decreaseFromCart(
                                    productItem
                                )
                            },
                            onClickRemoveProduct = { productItem ->
                                productListViewModel.removeFromCart(
                                    productItem
                                )
                            }
                        )
                    }

                    SheetContent.PRODUCT_DETAILS -> {
                        selectedProduct?.let { product ->
                            BottomSheetContent(
                                product = product,
                                cartItems = cartItems,
                                onClickAddProduct = { productListViewModel.addToCart(it) },
                                onClickDecreaseProduct = { productListViewModel.decreaseFromCart(it) }
                            )
                        }
                    }
                }
            },
            sheetState = bottomSheetState,
            sheetShape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp
            ),
            scrimColor = Color.Black.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxSize()
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "My Super App", fontWeight = FontWeight.W800) },
                        navigationIcon = {
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = null)
                            }
                        },
                        actions = {
                            BadgedBox(
                                badge = {
                                    if (cartItems.isNotEmpty()) {
                                        Badge(
                                            modifier = Modifier.offset(x = (-20).dp, y = 20.dp),
                                            containerColor = Red
                                        ) {
                                            Text(
                                                text = cartItems.sumOf { it.quantity }.toString(),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = White
                                            )
                                        }
                                    }
                                }
                            ) {
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            setSheetContent(SheetContent.CART)
                                            delay(200)
                                            bottomSheetState.show()
                                        }
                                    },
                                    modifier = Modifier.padding(10.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_walmart),
                                        contentDescription = null,
                                        tint = WalmartYellow
                                    )
                                }
                            }
                        },
                        colors = topAppBarColors(
                            containerColor = WalmartBlue,
                            titleContentColor = WalmartYellow,
                            navigationIconContentColor = WalmartYellow,
                            actionIconContentColor = WalmartBlue
                        ),
                    )
                },
                containerColor = BlueInk
            ) { padding ->
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Column {
                            val currentTopProduct = topProduct
                            if (currentTopProduct != null) {
                                TopProductComponent(
                                    topProduct = currentTopProduct,
                                    cartItems = cartItems,
                                    onClickAddProduct = {
                                        productListViewModel.addToCart(it)
                                    },
                                    onClickDecreaseProduct = {
                                        productListViewModel.decreaseFromCart(it)
                                    }
                                ) {
                                    setSelectedProduct(currentTopProduct)
                                    coroutineScope.launch {
                                        setSheetContent(SheetContent.PRODUCT_DETAILS)
                                        delay(200)
                                        bottomSheetState.show()
                                    }
                                }
                            }
                            LazyVerticalGrid(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .height(gridHeight),
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(bottom = 16.dp, top = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(14.dp),
                            ) {
                                items(products) { product ->
                                    ProductItemComponent(
                                        product = product,
                                        cartItems = cartItems,
                                        onClickAddProduct = { productListViewModel.addToCart(it) },
                                        onClickDecreaseProduct = { productListViewModel.decreaseFromCart(it) },
                                        onClickDetail = {
                                            setSelectedProduct(product)
                                            coroutineScope.launch {
                                                setSheetContent(SheetContent.PRODUCT_DETAILS)
                                                delay(200)
                                                bottomSheetState.show()
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

