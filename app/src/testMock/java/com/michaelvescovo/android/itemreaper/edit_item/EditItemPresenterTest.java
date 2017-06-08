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
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.USER_ID;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Michael Vescovo
 */

@RunWith(Parameterized.class)
public class EditItemPresenterTest {

    @Mock
    ImageFile mImageFile;
    @Mock
    Context mContext;
    @Mock
    Uri mUri;
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
    private Item mItem;

    public EditItemPresenterTest(Item item) {
        mItem = item;
    }

    @Parameterized.Parameters
    public static Iterable<?> data() {
        return Arrays.asList(
                ITEM_1,
                ITEM_2
        );
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mEditItemPresenter = new EditItemPresenter(mView, mRepository, mImageFile, USER_ID);
    }

    @Test
    public void noId_createsItem() {
        mEditItemPresenter.editItem(null);

        // Gets a new itemId
        verify(mRepository).getNewItemId(anyString(), mNewItemIdCallbackCaptor.capture());

        // New itemId comes back
        mNewItemIdCallbackCaptor.getValue().onNewItemIdLoaded(ITEM_1.getId());

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
        verify(mRepository).getItem(anyString(), anyString(), anyString(),
                mItemCallbackCaptor.capture());

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
    public void itemChanged_ItemValidated() {
        mEditItemPresenter.itemChanged();
        verify(mView).validateItem();
    }

    @Test
    public void takePicture_CreatesFileAndOpensCamera() throws IOException {
        mEditItemPresenter.takePicture(mContext);
        verify(mImageFile).create(any(Context.class), anyString(), anyString());
        verify(mView).openCamera(any(ImageFile.class));
    }

    @Test
    public void selectImage_SelectsImage() {
        mEditItemPresenter.selectImage();
        verify(mView).openImagePicker();
    }

    @Test
    public void imageAvailable_CompressesImage() {
        String imageUrl = "path/to/file";
        when(mImageFile.exists()).thenReturn(true);
        when(mImageFile.getPath()).thenReturn(imageUrl);

        mEditItemPresenter.imageAvailable(mImageFile);
        verify(mView).setProgressBar(true);
        verify(mView).setInteractionEnabled(false);
        verify(mView).removeImage();
        verify(mView).compressImage(anyString());
    }

    @Test
    public void imageAvailable_FileDoesNotExistShowsErrorUi() {
        when(mImageFile.exists()).thenReturn(false);

        mEditItemPresenter.imageAvailable(mImageFile);
        verify(mView).setProgressBar(false);
        verify(mView).setInteractionEnabled(true);
        verify(mView).showImageError();
    }

    @Test
    public void noImageAvailable_ShowsErrorUi() {
        mEditItemPresenter.imageCaptureFailed(mContext, mImageFile);

        verify(mView).showImageError();
        if (mImageFile.exists()) {
            verify(mImageFile).delete(any(Context.class));
        }
    }

    @Test
    public void imageSelected_CompressesImage() {
        mEditItemPresenter.imageSelected(mContext, mUri);
        verify(mView).setProgressBar(true);
        verify(mView).setInteractionEnabled(false);
        verify(mView).removeImage();
        verify(mView).compressImage(anyString());
    }

    @Test
    public void imageCompressed() {
        mEditItemPresenter.imageCompressed(anyString());
        verify(mView).setProgressBar(false);
        verify(mView).setInteractionEnabled(true);
        verify(mView).showImage(anyString());
    }

    @Test
    public void deleteImage_DeletesImage() {
        mEditItemPresenter.deleteImage();
        verify(mView).removeImage();
    }

    @Test
    public void deleteItem_DeletesItem() {
        mEditItemPresenter.deleteItem(mItem);
        verify(mRepository).deleteItem(anyString(), any(Item.class));
        verify(mView).passDeletedItemToItemsUi();
        verify(mView).showItemsUi();
    }
}
