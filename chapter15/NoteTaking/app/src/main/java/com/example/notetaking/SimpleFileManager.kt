package com.example.notetaking

import android.content.Context
import android.util.AtomicFile
import org.json.JSONObject
import java.io.File
import java.io.IOException

/** One JSON document per note. JSON escaping preserves multiline content and separator text. */
class SimpleFileManager(context: Context) {
    private val directory = File(context.filesDir, "notes")

    private fun noteFile(id: String): File {
        require(id.matches(Regex("[A-Za-z0-9-]+"))) { "Invalid note ID" }
        if (!directory.isDirectory && !directory.mkdirs()) throw IOException("Cannot create notes directory")
        return File(directory, "$id.json")
    }

    fun saveNote(note: Note): Boolean {
        return try {
            val data = JSONObject().put("id", note.id).put("title", note.title)
                .put("content", note.content).put("createdAt", note.createdAt)
                .put("updatedAt", note.updatedAt)
            val atomic = AtomicFile(noteFile(note.id))
            val stream = atomic.startWrite()
            try {
                stream.write(data.toString().toByteArray(Charsets.UTF_8))
                atomic.finishWrite(stream)
            } catch (error: Exception) {
                atomic.failWrite(stream)
                throw error
            }
            true
        } catch (error: Exception) {
            false
        }
    }

    fun loadNote(id: String): Note? {
        return try {
            val atomic = AtomicFile(noteFile(id))
            if (!atomic.baseFile.exists() && !File(atomic.baseFile.path + ".bak").exists()) return null
            val text = atomic.openRead().bufferedReader(Charsets.UTF_8).use { it.readText() }
            val data = JSONObject(text)
            Note(id, data.getString("title"), data.getString("content"),
                data.getLong("createdAt"), data.getLong("updatedAt"))
        } catch (error: Exception) {
            null
        }
    }

    fun getAllNoteIds(): List<String> {
        if (!directory.exists()) return emptyList()
        val files = directory.listFiles() ?: throw IOException("Cannot list notes")
        // Include interrupted-write backups so AtomicFile can recover them on read.
        return files.filter { it.isFile && (it.name.endsWith(".json") || it.name.endsWith(".json.bak")) }
            .map { it.name.removeSuffix(".bak").removeSuffix(".json") }.distinct().sorted()
    }

    fun deleteNote(id: String): Boolean {
        val file = noteFile(id)
        val existed = file.exists() || File(file.path + ".bak").exists()
        AtomicFile(file).delete()
        return existed && !file.exists() && !File(file.path + ".bak").exists()
    }
}
