package article

import java.io.File
import java.io.PrintWriter

class ArticleRepository {
    private val articles = mutableListOf<Article>()
    private var lastId = 0

    fun deleteArticle(article: Article) {
        articles.remove(article)
    }

    fun getArticleById(id: Int): Article? {
        for (article in articles) {
            if (article.id == id) {
                return article
            }
        }

        return null
    }

    fun addArticle(memberId: Int, title: String, body: String): Int {
        val id = ++lastId
        val regDate = Util.getNowDateStr()
        val updateDate = Util.getNowDateStr()

        articles.add(Article(id, regDate, updateDate, memberId, title, body))

        return id
    }

    fun addJson(memberId: Int, title: String, body: String): Int {

        val fileTree : FileTreeWalk = File("C:\\Users\\SBS-\\IdeaProjects\\file\\article").walk()

        val regDate = Util.getNowDateStr()
        val updateDate = Util.getNowDateStr()
        var cnt = 0

        for (file in fileTree) {
            if (file.name.contains("json")) {
                cnt++
            }
        }

        println("$cnt")
        cnt ++

        val path = "C:\\Users\\SBS-\\IdeaProjects\\file\\article\\${cnt}.json"
        val contents = "{\n" +
                "  \"id\":${cnt},\n" +
                "  \"regDate\": \"${regDate}\",\n" +
                "  \"updateDate\": \"${updateDate}\",\n" +
                "  \"memberId\": ${memberId},\n" +
                "  \"boardId\": 1,\n" +
                "  \"title\": \"${title}\",\n" +
                "  \"body\": \"${body}\"\n" +
                "}"

        val file = File(path)
        val printWriter = PrintWriter(file)
        printWriter.println(contents)
        printWriter.close()

        return cnt
    }

    fun makeTestArticles() {
        for (id in 1..20) {
            addArticle(id % 9 + 1, "제목_$id", "내용_$id")
        }
    }

    fun modifyArticle(id: Int, title: String, body: String) {
        val article = getArticleById(id)!!

        article.title = title
        article.body = body
        article.updateDate = Util.getNowDateStr()
    }

    fun getFilteredArticles(searchKeyword: String, page: Int, itemsCountInAPage: Int): List<Article> {
        val filtered1Articles = getSearchKeywordFilteredArticles(articles, searchKeyword)
        val filtered2Articles = getPageFilteredArticles(filtered1Articles, page, itemsCountInAPage)

        return filtered2Articles
    }

    private fun getSearchKeywordFilteredArticles(articles: List<Article>, searchKeyword: String): List<Article> {
        val filteredArticles = mutableListOf<Article>()

        for (article in articles) {
            if (article.title.contains(searchKeyword)) {
                filteredArticles.add(article)
            }
        }

        return filteredArticles
    }

    private fun getPageFilteredArticles(articles: List<Article>, page: Int, itemsCountInAPage: Int): List<Article> {
        val filteredArticles = mutableListOf<Article>()

        val offsetCount = (page - 1) * itemsCountInAPage

        val startIndex = articles.lastIndex - offsetCount
        var endIndex = startIndex - (itemsCountInAPage - 1)

        if (endIndex < 0) {
            endIndex = 0
        }

        for (i in startIndex downTo endIndex) {
            filteredArticles.add(articles[i])
        }

        return filteredArticles
    }
}