package com.mywalmartapp.ui.productList.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun BottomSheetContent(
    product: ProductItem?,
    cartItems: List<CartItem>,
    onClickAddProduct: (ProductItem) -> Unit,
    onClickDecreaseProduct: (ProductItem) -> Unit,
    ) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxHeight(fraction = 0.8f)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        val (imageContent, productContent, productDescription, bottomContent) = createRefs()

        Image(
            painter = rememberAsyncImagePainter(product?.image),
            contentDescription = null,
            modifier = Modifier
                .padding(2.dp)
                .height(220.dp)
                .width(160.dp)
                .aspectRatio(1f)
                .constrainAs(imageContent) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            contentScale = ContentScale.Inside
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .constrainAs(productContent) {
                    top.linkTo(imageContent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Text(
                text = product?.title.orEmpty(),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(0.7f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(0.05f))
            Text(
                text = "$" + product?.price,
                color = Orange,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
        }
        Text(
            text = product?.description.orEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(productDescription) {
                    top.linkTo(productContent.bottom, margin = 4.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            maxLines = 9,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .constrainAs(bottomContent) {
                    bottom.linkTo(parent.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            val  rating = product?.rating?.rate?.toFloat() ?: 0f
            RatingBar(rating)
            Spacer(modifier = Modifier.weight(1f))
            val existingItem = cartItems.find { it.product.id == product?.id }
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
                        if (product != null) {
                            onClickAddProduct(product)
                        }
                    }
            )
        }
    }
}