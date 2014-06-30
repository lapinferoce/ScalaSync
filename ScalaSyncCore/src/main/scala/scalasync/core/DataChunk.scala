package scalasync.core

/**
 * Created by lapinferoce on 6/25/14.
 */
case class DataChunk(file:RepoFile,bytesRead:Array[Byte],readsize:Int,isLast:Boolean)
//case class CommitAllChunks(file:RepoFile)
