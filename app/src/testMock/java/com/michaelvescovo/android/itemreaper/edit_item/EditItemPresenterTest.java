package com.michaelvescovo.android.itemreaper.edit_item;

import android.content.Context;
import android.net.Uri;

import com.michaelvescovo.android.itemreaper.SharedPreferencesHelper;
import com.michaelvescovo.android.itemreaper.data.DataSource;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.data.Repository;
import com.michaelvescovo.android.itemreaper.util.ImageFile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;

import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_1;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_2;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_ID_1;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Michael Vescovo
 */

@RunWith(Parameterized.class)
public class EditItemPresenterTest {

    private EditItemPresenter mEditItemPresenter;

    @Mock
    private EditItemContract.View mView;

    @Mock
    private Repository mRepository;

    @Mock
    private SharedPreferencesHelper mSharedPreferencesHelper;

    @Captor
    private ArgumentCaptor<DataSource.GetNewItemIdCallback> mNewItemIdCallbackCaptor;

    @Captor
    private ArgumentCaptor<DataSource.GetItemCallback> mItemCallbackCaptor;

    @Mock
    ImageFile mImageFile;

    @Mock
    Context mContext;

    @Mock
    Uri mUri;

    @Parameterized.Parameters
    public static Iterable<?> data() {
        return Arrays.asList(
                ITEM_1,
                ITEM_2
        );
    }

    private Item mItem;

    public EditItemPresenterTest(Item item) {
        mItem = item;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mEditItemPresenter = new EditItemPresenter(mView, mRepository, mSharedPreferencesHelper,
                mImageFile);
    }

    @Test
    public void noId_createsItem() {
        mEditItemPresenter.editItem(null);

        // Gets a new itemId
        verify(mRepository).getNewItemId(anyString(), mNewItemIdCallbackCaptor.capture());

        // New itemId comes back
        mNewItemIdCallbackCaptor.getValue().onNewItemIdLoaded(ITEM_ID_1);

        // Set new itemId in view
        verify(mView).setNewItemId(anyString());

        // Set default dates
        verify(mView).setDefaultDates();
    }

    @Test
    public void validId_ItemIsLoadedForEditing() {
        mEditItemPresenter.editItem(mItem.getId());

        // Does not get new ID
        verify(mRepository, never()).getNewItemId(anyString(),
                any(DataSource.GetNewItemIdCallback.class));

        // Gets the existing item
        verify(mRepository).getItem(anyString(), anyString(), mItemCallbackCaptor.capture());

        // New item comes back
        mItemCallbackCaptor.getValue().onItemLoaded(mItem);

        // Displays the existing item in the view
        verify(mView).showExistingItem(any(Item.class));
    }

    @Test
    public void itemChanged_SavesItem() {
        mEditItemPresenter.saveItem(mItem);
        verify(mRepository).saveItem(anyString(), any(Item.class));
    }

    @Test
    public void doneEditing_ShowsItemsUi() {
        mEditItemPresenter.doneEditing();
        verify(mView).showItemsUi();
    }

    @Test
    public void takePicture_CreatesFileAndOpensCamera() throws IOException {
        mEditItemPresenter.takePicture(mContext);
        verify(mImageFile).create(any(Context.class), anyString(), anyString());
        verify(mView).openCamera(any(ImageFile.class));
    }

    @Test
    public void imageAvailable_ShowsImage() {
        String imageUrl = "path/to/file";
        when(mImageFile.exists()).thenReturn(true);
        when(mImageFile.getPath()).thenReturn(imageUrl);

        mEditItemPresenter.imageAvailable(mContext, mImageFile, "");

        verify(mView).showImage(contains(imageUrl));
    }

    @Test
    public void imageAvailable_FileDoesNotExistShowsErrorUi() {
        when(mImageFile.exists()).thenReturn(false);

        mEditItemPresenter.imageAvailable(mContext, mImageFile, "");

        verify(mView).showImageError();
        verify(mImageFile).delete();
    }

    @Test
    public void noImageAvailable_ShowsErrorUi() {
        mEditItemPresenter.imageCaptureFailed();

        verify(mView).showImageError();
        verify(mImageFile).delete();
    }

    @Test
    public void itemChanged_ItemValidated() {
        mEditItemPresenter.itemChanged();
        verify(mView).validateItem();
    }

    @Test
    public void selectImage_CreatesFileAndSelectsImage() {
        mEditItemPresenter.selectImage(mContext);
        verify(mView).openImagePicker();
        mEditItemPresenter.imageSelected(mContext, ITEM_1.getImageUrl(), mUri);
        verify(mView).showImage(anyString());
    }

    @Test
    public void deleteImage_DeletesImage() {
        mEditItemPresenter.deleteImage(mContext, "https://firebasestorage");
        verify(mView).removeImage();
    }

    @Test
    public void clearEditItemCache_RefersToRepository() {
        mEditItemPresenter.clearEditItemCache(ITEM_ID_1);
        verify(mRepository).refreshItem(anyString());
    }

    @Test
    public void deleteItem_DeletesItem() {
        mEditItemPresenter.deleteItem(mItem);
        verify(mRepository).deleteItem(anyString(), any(Item.class));
        verify(mView).passDeletedItemToItemsUi();
        verify(mView).showItemsUi();
    }
}
