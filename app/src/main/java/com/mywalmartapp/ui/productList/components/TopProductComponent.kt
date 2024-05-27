package com.mywalmartapp.ui.productList.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.mywalmartapp.ui.theme.Orange
import com.mywalmartapp.ui.theme.WalmartYellow

@Composable
fun TopProductComponent(
    topProduct: ProductItem?,
    cartItems: List<CartItem>,
    onClickAddProduct: (ProductItem) -> Unit,
    onClickDecreaseProduct: (ProductItem) -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxHeight()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
    ) {
        Image(
            painter = rememberAsyncImagePainter(topProduct?.image),
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
            ) {
                Text(
                    text = "Destacado",
                    color = WalmartYellow,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W700,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = topProduct?.title.orEmpty(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = "$" + topProduct?.price,
                modifier = Modifier
                    .constrainAs(priceContent) {
                        end.linkTo(parent.end, margin = 10.dp)
                        bottom.linkTo(addButton.top, margin = 6.dp)
                    },
                color = Orange,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
            Row(
                modifier = Modifier
                    .constrainAs(addButton) {
                        end.linkTo(parent.end, margin = 10.dp)
                        bottom.linkTo(parent.bottom, margin = 10.dp)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                val existingItem = cartItems.find { it.product.id == topProduct?.id }
                if (existingItem != null) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_decrease_product),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clickable { onClickDecreaseProduct(existingItem.product) }
                    )
                    Text(
                        text = existingItem.quantity.toString(),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_add_product),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            if (topProduct != null) {
                                onClickAddProduct(topProduct)
                            }
                        }
                )
            }
        }
    }
}