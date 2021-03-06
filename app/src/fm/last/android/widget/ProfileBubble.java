/***************************************************************************
 *   Copyright 2005-2009 Last.fm Ltd.                                      *
 *   Portions contributed by Casey Link, Lukasz Wisniewski,                *
 *   Mike Jennings, and Michael Novak Jr.                                  *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.         *
 ***************************************************************************/
package fm.last.android.widget;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import fm.last.android.R;
import fm.last.android.RemoteImageHandler;
import fm.last.android.RemoteImageView;
import fm.last.android.Worker;
import fm.last.api.User;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProfileBubble extends LinearLayout {

    User mUser;
    TextView mFirst;
    TextView mSecond;
    RemoteImageView mAvatar;
    private Worker mProfileImageWorker;
    private RemoteImageHandler mProfileImageHandler;

    public ProfileBubble(Context context) {
        super(context);
        init();
    }

    public ProfileBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.profile_bubble, this);
        // we did do this but it looks wrong due to lack of 
        //this.setBackgroundResource(R.drawable.profile_bubble_bg);

        mFirst  = (TextView) findViewById(R.id.profile_username);
        mSecond = (TextView) findViewById(R.id.profile_meta);
        mAvatar = (RemoteImageView) findViewById(R.id.profile_avatar);

        mSecond.setText("Loading profile...");
    }

    public void setUser(User user) {
        mUser = user;

        mAvatar.setDefaultImage( R.drawable.profile_unknown );
        
        if(user.getRealName() == null)
            mFirst.setText(user.getName());
        else 
            mFirst.setText(user.getRealName());
        
        
        List<String> seconds = new ArrayList<String>();

        if (user.getAge() != null) seconds.add( user.getAge() );
        if (user.getGender() != null) seconds.add( user.getGender() );
        if (user.getCountry() != null) seconds.add( user.getCountry() );
        
        String second = "";
        for(String s: seconds)
        	second = s + ", ";

        int playcount = Integer.parseInt(mUser.getPlaycount());
        NumberFormat format = NumberFormat.getNumberInstance();
        String count = format.format( playcount );
        String plays = count + " plays";
        if(mUser.getJoinDate() != null)
            plays += " since " + mUser.getJoinDate();
        mSecond.setText(second + plays);

        if( mUser.getImages().length > 0 ) {
            mProfileImageWorker = new Worker("profile image worker");
            mProfileImageHandler = new RemoteImageHandler(mProfileImageWorker.getLooper(), mHandler);
            mProfileImageHandler.removeMessages( RemoteImageHandler.GET_REMOTE_IMAGE );
            mProfileImageHandler.obtainMessage( RemoteImageHandler.GET_REMOTE_IMAGE,
                    mUser.getImages()[0].getUrl()).sendToTarget();
        }
    }

    private final Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
            case RemoteImageHandler.REMOTE_IMAGE_DECODED:
                mProfileImageWorker.quit();
                if (mAvatar != null) {
                    mAvatar.setArtwork((Bitmap) msg.obj);
                    mAvatar.invalidate();
                }
                break;
            default:
                break;
            }
        }
    };
}
