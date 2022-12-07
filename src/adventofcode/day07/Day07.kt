package adventofcode.day07

import adventofcode.AdventOfCodeSolution
import java.util.*

fun main() {
    Solution.solve()
}

object Solution : AdventOfCodeSolution<Int>() {
    override fun solve() {
        solve(7, 95437)
    }

    override fun part1(input: List<String>): Int {
        return getDirectorySizes(input)
            .filter { it.second <= 100000 }
            .map {
                debug(it)
                it
            }
            .sumOf { it.second }
    }

    private fun getDirectorySizes(input: List<String>): List<Pair<String, Int>> = getDirectories(input)
        .map { Pair(it.name, it.calculateSize()) }

    private fun getDirectories(input: List<String>): List<Directory> {
        val iterator = input.iterator()
        var lineNumber = 0
        val dirs = mutableMapOf<String, Directory>()
        val getDir = { path: String ->
            dirs[path] ?: run {
                val dir = Directory(path)
                dirs.putIfAbsent(path, dir)
                dir
            }
        }
        val currentPath = Stack<Directory>()
        currentPath.push(getDir("/"))

        while (iterator.hasNext()) {
            lineNumber++
            with(iterator.next()) {
                val currentDir = currentPath.peek()
                when {
                    equals("$ cd /") -> {
                        currentPath.empty()
                        currentPath.push(getDir("/"))
                    }

                    equals("$ cd ..") -> {
                        currentPath.pop()
                    }

                    startsWith("$ cd ") -> {
                        currentPath.push(getDir("${currentDir.name}/${this.substring(5)}"))
                    }

                    equals("$ ls") -> {}
                    startsWith("dir ") -> {
                        currentDir.addDir(getDir("${currentDir.name}/${this.substring(4)}"))
                    }

                    else -> {
                        val (size, file) = this.split(" ", limit = 2)
                        currentDir.addFile(file, size.toInt())
                    }
                }
            }
        }

        return dirs.values.toList()
    }

    class Directory(
        val name: String,
        private val dirs: ArrayList<Directory> = arrayListOf(),
        private val files: MutableMap<String, Int> = mutableMapOf()
    ) {
        fun addDir(dir: Directory) {
            dirs.add(dir)
        }

        fun addFile(name: String, size: Int) {
            files[name] = size
        }

        fun calculateSize(): Int = dirs.sumOf { it.calculateSize() } + files.values.sum()
    }
}
