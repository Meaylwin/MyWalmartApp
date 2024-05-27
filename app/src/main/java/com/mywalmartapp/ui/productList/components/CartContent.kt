package com.mywalmartapp.ui.productList.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.myproductswallmart.R
import com.mywalmartapp.ui.cart.entities.CartItem
import com.mywalmartapp.ui.productList.entities.ProductItem
import com.mywalmartapp.ui.theme.LightGreen
import com.mywalmartapp.ui.theme.Orange

@Composable
fun CartSheetContent(
    cartItems: List<CartItem>,
    cartTotal: Float,
    onClickAddProduct: (ProductItem) -> Unit,
    onClickDecreaseProduct: (ProductItem) -> Unit,
    onClickRemoveProduct: (ProductItem) -> Unit
) {
    if (cartItems.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxHeight(fraction = 0.75f)
                .padding(20.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tu carrito está vacío",
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_cart),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        }
        Spacer(modifier = Modifier.padding(top = 94.dp))
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight(fraction = 0.75f)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(cartItems) { cartItem ->
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(cartItem.product.image),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(2.dp)
                            .height(220.dp)
                            .width(160.dp)
                            .aspectRatio(1f),
                        contentScale = ContentScale.Inside
                    )

                    ConstraintLayout(
                        modifier = Modifier
                            .height(220.dp)
                            .fillMaxWidth()
                    ) {
                        val (productContent, priceContent, addButton) = createRefs()
                        Column(
                            modifier = Modifier
                                .constrainAs(productContent) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                                .padding(horizontal = 6.dp, vertical = 10.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = cartItem.product.title,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.padding(2.dp))
                            Text(
                                text = "$" + cartItem.product.price,
                                color = Orange,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(priceContent) {
                                    end.linkTo(parent.end)
                                    bottom.linkTo(addButton.top, margin = 6.dp)
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = { onClickRemoveProduct(cartItem.product) },
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red
                                ),
                                modifier = Modifier
                                    .width(100.dp)
                            ) {
                                Text(text = "Eliminar")
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(addButton) {
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom, margin = 10.dp)
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_decrease_product),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable { onClickDecreaseProduct(cartItem.product) }
                            )
                            Text(
                                text = cartItem.quantity.toString(),
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Image(
                                painter = painterResource(id = R.drawable.ic_add_product),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable { onClickAddProduct(cartItem.product) }
                            )
                        }

                    }
                }
                Divider(color = Color.LightGray, thickness = 1.dp, startIndent = 5.dp)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total: $$cartTotal",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            Button(
                onClick = {  },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightGreen
                ),
                modifier = Modifier
                    .padding(8.dp)
                    .width(400.dp)
            ) {
                Text(
                    text = "Comprar",
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp
                )
            }
        }
    }
}
