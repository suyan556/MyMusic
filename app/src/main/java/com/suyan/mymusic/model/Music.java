package com.suyan.mymusic.model;

/**
 * Created by Yan on 2016/9/12.
 */
public class Music {
    /*private String musicPath;
    private String musicName;
    private String musicAlbum;
    private String musicArtist;
    private String musicAlbumKey;
    private String musicAlbumArtPath;*/

    private String title; // 歌曲名称
    private String album; // 专辑
    private long albumId;// 专辑ID
    private String displayName; // 显示名称
    private String artist; // 歌手名称
    private long duration; // 歌曲时长
    private long size; // 歌曲大小
    private String url; // 歌曲路径
    private String lrcTitle; // 歌词名称
    private String lrcSize; // 歌词大小
    private long musicId; //音乐ID

    public Music(){
        super();
    }

    public Music(String title, String album, long albumId, String displayName, String artist, long duration, long size, String url, String lrcTitle, String lrcSize, long musicId) {
        this.title = title;
        this.album = album;
        this.albumId = albumId;
        this.displayName = displayName;
        this.artist = artist;
        this.duration = duration;
        this.size = size;
        this.url = url;
        this.lrcTitle = lrcTitle;
        this.lrcSize = lrcSize;
        this.musicId = musicId;
    }

    @Override
    public String toString() {
        return "Music{" +
                "title='" + title + '\'' +
                ", album='" + album + '\'' +
                ", albumId=" + albumId +
                ", displayName='" + displayName + '\'' +
                ", artist='" + artist + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", url='" + url + '\'' +
                ", lrcTitle='" + lrcTitle + '\'' +
                ", lrcSize='" + lrcSize + '\'' +
                ", musicId=" + musicId +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLrcTitle() {
        return lrcTitle;
    }

    public void setLrcTitle(String lrcTitle) {
        this.lrcTitle = lrcTitle;
    }

    public String getLrcSize() {
        return lrcSize;
    }

    public void setLrcSize(String lrcSize) {
        this.lrcSize = lrcSize;
    }

    public long getMusicId() {
        return musicId;
    }

    public void setMusicId(long musicId) {
        this.musicId = musicId;
    }
}
