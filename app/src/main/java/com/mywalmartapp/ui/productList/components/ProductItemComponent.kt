package com.mywalmartapp.ui.productList.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.myproductswallmart.R
import com.mywalmartapp.ui.productList.entities.ProductItem
import com.mywalmartapp.ui.theme.Orange

@Composable
fun ProductItemComponent(
    product: ProductItem,
    onClickAddProduct: () -> Unit,
    onClickDetail: () -> Unit)
{
    ConstraintLayout(
        modifier = Modifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClickDetail() }
    ) {
        val (imageContent, productContent, bottomContent) = createRefs()

        Image(
            painter = rememberAsyncImagePainter(product.image),
            contentDescription = null,
            modifier = Modifier
                .padding(2.dp)
                .aspectRatio(2f)
                .constrainAs(imageContent) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            contentScale = ContentScale.Inside,
            alignment = Alignment.Center
        )
        Column(
            modifier = Modifier.constrainAs(productContent) {
                top.linkTo(imageContent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            Text(
                text = product.title,
                modifier = Modifier.padding(horizontal = 4.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 2.dp)
                .constrainAs(bottomContent) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Text(
                text = "\$${product.price}",
                color = Orange,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.ic_add_product),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onClickAddProduct() }
            )
        }
    }
}