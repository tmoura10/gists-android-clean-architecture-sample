package br.com.tmoura.gists.domain.interactor.impl

import br.com.tmoura.gists.domain.ReactiveTransformer
import br.com.tmoura.gists.domain.SchedulersProvider
import br.com.tmoura.gists.domain.factory.GistFactory
import br.com.tmoura.gists.domain.interactor.GetGistInteractor
import br.com.tmoura.gists.domain.model.Gist
import br.com.tmoura.gists.domain.repository.GistRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class GetGistInteractorImplTest {

    private lateinit var gistRepository: GistRepository
    private lateinit var schedulersProvider: SchedulersProvider
    private lateinit var getGistInteractor: GetGistInteractorImpl
    private lateinit var transformer: ReactiveTransformer<Gist>
    private val factory = GistFactory()

    @Before
    fun `run before each test`() {
        gistRepository = mock()
        schedulersProvider = mock()
        transformer = mock()
        getGistInteractor = GetGistInteractorImpl(gistRepository = gistRepository,
                schedulersProvider = schedulersProvider)

        whenever(schedulersProvider.buildTransformer<Gist>()).thenReturn(transformer)
    }

    @Test
    fun `gist is retrieved with success`() {
        val gist = factory.create()
        val single = Single.just(gist)

        whenever(gistRepository.getGist(any())).thenReturn(single)
        whenever(transformer.apply(single)).thenReturn(single)

        val subscriber = getGistInteractor.execute(GetGistInteractor.Params(id = "1")).test()

        subscriber.assertNoErrors()
        subscriber.assertValue(gist)
    }

    @Test
    fun `interactor is executed with right transformer`() {
        val gist = factory.create()
        val single = Single.just(gist)

        whenever(gistRepository.getGist(any())).thenReturn(single)
        whenever(transformer.apply(single)).thenReturn(single)

        getGistInteractor.execute(GetGistInteractor.Params(id = "1")).test()

        verify(transformer).apply(single)
    }

    @Test
    fun `exceptions are handled on Single onError`() {
        val exception = Exception("test exception")
        val single = Single.error<Gist>(exception)

        whenever(gistRepository.getGist(any())).thenReturn(single)
        whenever(transformer.apply(single)).thenReturn(single)

        val subscriber = getGistInteractor.execute(GetGistInteractor.Params(id = "1")).test()

        subscriber.assertError(exception)
    }

}