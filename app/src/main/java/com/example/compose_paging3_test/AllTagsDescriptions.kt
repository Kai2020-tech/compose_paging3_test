package com.cxc.cxctoday.ui.home.recommend.hot_tags

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.compose_paging3_test.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AllTagsScreen(
    vm: HomeAllTagsViewModel? = null,
) {
    val allTags = vm?.getAllTagPaging()?.collectAsLazyPagingItems()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.real_white))
    ) {
        ScreenTitle(
            modifier = Modifier.fillMaxWidth(),
            "全站標籤"
        ) { }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(R.color.light_white))
                .padding(horizontal = 16.dp)
        ) {
            items(
                count = allTags?.itemCount ?: 0,
                key = allTags?.itemKey { tag ->
                    tag.name
                }
            ) {
                val item = allTags?.itemSnapshotList?.getOrNull(it)
                Tag(
                    item?.name ?: return@items,
                    item.count.toString()
                )
            }
        }

    }
}

@Composable
fun Tag(tagName: String, tagNum: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(id = R.color.real_white))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Image(painter = painterResource(id = R.drawable.icon_label_gray), contentDescription = "")
        Spacer(modifier = Modifier.width(6.dp))
        MyText(text = tagName, color = R.color.blue_grey)
        Spacer(modifier = Modifier.width(12.dp))
        MyText(text = tagNum, color = R.color.blue_grey_30)
    }
}

@Composable
fun ScreenTitle(
    modifier: Modifier = Modifier,
    title: String,
    onCloseClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.btn_close_white_circle
            ),
            contentDescription = "close",
            modifier = Modifier
                .padding(start = 16.dp)
                .clickable { onCloseClick() }
        )

        Text(
            text = title,
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight(600),
                color = colorResource(id = R.color.dark),
            ),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
        )
    }
}

@Composable
fun MyText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: Int = 14,
    fontWeight: Int = 400,
    color: Int = R.color.dark,
    textAlign: TextAlign? = null,
    maxLine: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis,
) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = fontSize.sp,
            fontWeight = FontWeight(fontWeight),
            color = colorResource(id = color),
        ),
        maxLines = maxLine,
        overflow = overflow,
        modifier = modifier,
        textAlign = textAlign
    )
}


@Preview(showBackground = true)
@Composable
fun TagPreview() {
    Tag("標籤", "1998")
}
